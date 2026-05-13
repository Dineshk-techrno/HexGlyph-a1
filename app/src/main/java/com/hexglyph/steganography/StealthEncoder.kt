package com.hexglyph.steganography

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.hexglyph.crypto.CryptoEngine
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.zip.CRC32
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * StealthEncoder
 *
 * Encodes a plaintext message into an ordinary-looking carrier image by:
 *
 *  1. Encrypting the message via [CryptoEngine.encodeFull] (unchanged crypto pipeline).
 *  2. Wrapping the encrypted block in a HXGS-prefixed stealth payload structure.
 *  3. Optionally re-encrypting the wrapper with a Group Code (outer layer).
 *  4. Embedding the resulting bit stream into the carrier using randomised LSB
 *     steganography, preferring high-texture / edge regions (RegionAnalyzer).
 *  5. Also embedding the HXGS signature at a canonical well-known slot sequence
 *     so [StealthDecoder] / [SignatureDetector] can find it quickly.
 *
 * Payload wire format (all fields big-endian):
 *
 *   [0..3]   magic        = 0x48 0x58 0x47 0x53  ("HXGS")
 *   [4]      version      = 0x01
 *   [5]      flags        = bit-0: groupProtected
 *   [6..9]   payloadLen   = uint32 — byte count of encryptedBlock
 *   [10..13] checksum     = CRC32 of encryptedBlock (uint32)
 *   [14..]   encryptedBlock (305 bytes from CryptoEngine.encodeFull)
 *
 * Total header overhead: 14 bytes → total minimum payload: 14 + 305 = 319 bytes.
 */
class StealthEncoder @Inject constructor(
    private val cryptoEngine:    CryptoEngine,
    private val carrierSelector: CarrierSelector
) {

    companion object {
        const val VERSION: Byte = 0x01
        const val FLAG_GROUP_PROTECTED: Byte = 0x01
        private const val HEADER_SIZE = 14
    }

    /**
     * Encode [plaintext] into a stealth image.
     *
     * @param context          Android context.
     * @param plaintext        Message to hide.
     * @param groupCode        Shared secret for [CryptoEngine.encodeFull].
     * @param groupProtected   When true, wrap payload in an additional Group-Code layer.
     * @param carrierUri       Optional user-supplied carrier image.
     * @return Mutable [Bitmap] with hidden payload embedded, ready to export as PNG.
     */
    fun encode(
        context:        Context,
        plaintext:      String,
        groupCode:      String,
        groupProtected: Boolean = false,
        carrierUri:     Uri?    = null
    ): Bitmap {
        val plaintextBytes = plaintext.toByteArray(Charsets.UTF_8)
        require(plaintextBytes.size <= 197) {
            "Plaintext too long: ${plaintextBytes.size} bytes (max 197)"
        }

        // 1. Core encryption (reuses existing pipeline unchanged)
        val encryptedBlock = cryptoEngine.encodeFull(plaintextBytes, groupCode)

        // 2. Optional Group Protection outer wrap
        val finalBlock = if (groupProtected) {
            applyGroupProtection(encryptedBlock, groupCode)
        } else {
            encryptedBlock
        }

        // 3. Build payload header + body
        val payload = buildPayload(finalBlock, groupProtected)

        // 4. Select carrier
        val carrier = carrierSelector.selectCarrier(context, carrierUri, payload.size)
            ?: throw IllegalStateException(
                "No suitable carrier image found for payload of ${payload.size} bytes. " +
                "Try a larger or more textured carrier image."
            )

        // 5. Embed HXGS signature at canonical positions
        SignatureDetector.embedSignature(carrier)

        // 6. Embed payload using randomised LSB steganography
        embedPayload(carrier, payload, groupCode)

        return carrier
    }

    // ── Payload construction ──────────────────────────────────────────────────

    private fun buildPayload(encryptedBlock: ByteArray, groupProtected: Boolean): ByteArray {
        val crc = CRC32().apply { update(encryptedBlock) }.value.toInt()
        val flags: Byte = if (groupProtected) FLAG_GROUP_PROTECTED else 0x00

        val buf = ByteBuffer.allocate(HEADER_SIZE + encryptedBlock.size)
        buf.put(SignatureDetector.STEALTH_MAGIC)        // [0..3]  magic
        buf.put(VERSION)                                // [4]     version
        buf.put(flags)                                  // [5]     flags
        buf.putInt(encryptedBlock.size)                 // [6..9]  payloadLen
        buf.putInt(crc)                                 // [10..13] checksum
        buf.put(encryptedBlock)                         // [14..]  data
        return buf.array()
    }

    // ── Optional Group Protection outer layer ─────────────────────────────────
    // Wraps the encrypted block with a direct AES-GCM pass keyed from
    // SHA-256(groupCode). Works on arbitrary-length data — no 197-byte limit.
    // Wire format: [12-byte IV] [AES-GCM ciphertext + 16-byte tag]

    private fun applyGroupProtection(block: ByteArray, groupCode: String): ByteArray {
        val key = deriveGroupKey(groupCode)
        val iv  = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, iv))
        val ciphertext = cipher.doFinal(block)
        // Prepend IV so decoder can recover it
        val out = ByteArray(12 + ciphertext.size)
        System.arraycopy(iv,         0, out, 0,  12)
        System.arraycopy(ciphertext, 0, out, 12, ciphertext.size)
        return out
    }

    private fun deriveGroupKey(groupCode: String): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(groupCode.toByteArray(Charsets.UTF_8))

    // ── LSB embedding ─────────────────────────────────────────────────────────

    private fun embedPayload(bitmap: Bitmap, payload: ByteArray, seed: String) {
        // Derive seed from group code + stable image fingerprint (high bits of first
        // 64 pixels — untouched by LSB edits).  Must exactly mirror StealthDecoder.
        val seedBytes = deriveEmbeddingSeedFromImage(seed, bitmap)
        val mapper    = RandomPixelMapper(seedBytes)

        // Build region-aware slot sequence: prefer textured regions.
        val mask      = RegionAnalyzer.buildEmbeddingMask(bitmap)
        val allSlots  = mapper.buildSlotSequence(bitmap.width, bitmap.height)

        // Partition: textured slots first, then flat slots (fallback).
        val preferred = allSlots.filter { s ->
            mask[s.y * bitmap.width + s.x]
        }
        val fallback  = allSlots.filter { s ->
            !mask[s.y * bitmap.width + s.x]
        }
        val orderedSlots = preferred + fallback

        val requiredBits = payload.size * 8
        check(orderedSlots.size >= requiredBits) {
            "Carrier too small: need $requiredBits slots, have ${orderedSlots.size}"
        }

        // Write the embedding-seed length prefix (4 bytes) + payload
        val writer = BitStreamWriter(bitmap, orderedSlots)
        // Write payload length as uint32 BE so the decoder knows how many bytes to read
        val lenBytes = ByteBuffer.allocate(4).putInt(payload.size).array()
        writer.writeBytes(lenBytes)
        writer.writeBytes(payload)
    }

    private fun deriveEmbeddingSeedFromImage(groupCode: String, bitmap: Bitmap): ByteArray {
        // Use R channel (bits 23-16 of ARGB_8888 pixel, i.e. `ushr 16 and 0xFF`).
        // LSB embedding only modifies bit-0 of each channel. The R channel byte
        // has bit-0 at position 16 in the pixel int, so `ushr 16 and 0xFF` still
        // captures the full R byte including its LSB — but we take `ushr 17 and 0x7F`
        // to use bits 23-17 only (the 7 high bits of R), which are NEVER touched by
        // single-LSB embedding. This guarantees the fingerprint is identical before
        // and after embedding, including after SignatureDetector.embedSignature().
        val count       = minOf(64, bitmap.width * bitmap.height)
        val fingerprint = ByteArray(count) { i ->
            val x = i % bitmap.width
            val y = i / bitmap.width
            ((bitmap.getPixel(x, y) ushr 17) and 0x7F).toByte()  // bits 23-17 of R, immune to LSB
        }
        val groupBytes = groupCode.toByteArray(Charsets.UTF_8)
        val combined   = concatBytes(groupBytes, fingerprint)
        return MessageDigest.getInstance("SHA-256").digest(combined)
    }

    private fun concatBytes(a: ByteArray, b: ByteArray): ByteArray {
        val result = ByteArray(a.size + b.size)
        System.arraycopy(a, 0, result, 0, a.size)
        System.arraycopy(b, 0, result, a.size, b.size)
        return result
    }
}
