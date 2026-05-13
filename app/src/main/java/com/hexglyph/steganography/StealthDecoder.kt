package com.hexglyph.steganography

import android.graphics.Bitmap
import com.hexglyph.crypto.CryptoEngine
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.zip.CRC32
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * StealthDecoder
 *
 * Reverses [StealthEncoder]:
 *
 *  1. Confirms the HXGS signature is present in the carrier bitmap.
 *  2. Derives the same deterministic slot sequence used during encoding.
 *  3. Reads payload length (4-byte prefix) then the full payload bytes.
 *  4. Validates the HXGS magic, version, and CRC-32 checksum.
 *  5. Optionally strips Group Protection outer layer.
 *  6. Passes the inner encrypted block to [CryptoEngine.decodeFull].
 *  7. Returns the recovered plaintext string.
 *
 * All exceptions are propagated to the caller (ViewModel) for UI display.
 */
class StealthDecoder @Inject constructor(
    private val cryptoEngine: CryptoEngine
) {

    companion object {
        private const val HEADER_SIZE = 14
    }

    /**
     * Attempt to extract and decrypt a hidden HexGlyph payload from [bitmap].
     *
     * @param bitmap     Carrier image (must be ARGB_8888).
     * @param groupCode  Shared secret — must match the one used during encoding.
     * @return Decrypted plaintext string.
     * @throws IllegalStateException / IllegalArgumentException on any failure.
     */
    fun decode(bitmap: Bitmap, groupCode: String): String {
        // 1. Quick signature check
        require(SignatureDetector.hasSignature(bitmap)) {
            "No hidden HexGlyph payload detected in this image."
        }

        // 2. Build the same slot sequence
        val seedBytes   = deriveEmbeddingSeed(groupCode, bitmap)
        val mapper      = RandomPixelMapper(seedBytes)
        val mask        = RegionAnalyzer.buildEmbeddingMask(bitmap)
        val allSlots    = mapper.buildSlotSequence(bitmap.width, bitmap.height)

        val preferred   = allSlots.filter { s -> mask[s.y * bitmap.width + s.x] }
        val fallback    = allSlots.filter { s -> !mask[s.y * bitmap.width + s.x] }
        val orderedSlots = preferred + fallback

        val reader = BitStreamReader(bitmap, orderedSlots)

        // 3. Read length prefix (uint32 BE)
        val lenBytes    = reader.readBytes(4)
        val payloadSize = ByteBuffer.wrap(lenBytes).int
        require(payloadSize in 1..65_536) {
            "Invalid payload size: $payloadSize — wrong group code or corrupt image?"
        }

        // 4. Read payload bytes
        val payload = reader.readBytes(payloadSize)

        // 5. Parse and validate header
        val parsed = parsePayload(payload)

        // 6. Strip group protection if flagged
        val encryptedBlock = if (parsed.groupProtected) {
            stripGroupProtection(parsed.encryptedBlock, groupCode)
        } else {
            parsed.encryptedBlock
        }

        // 7. Decrypt via existing CryptoEngine pipeline
        val plainBytes = cryptoEngine.decodeFull(encryptedBlock, groupCode)
        return plainBytes.toString(Charsets.UTF_8)
    }

    // ── Payload parsing ───────────────────────────────────────────────────────

    private data class ParsedPayload(
        val groupProtected: Boolean,
        val encryptedBlock: ByteArray
    )

    private fun parsePayload(payload: ByteArray): ParsedPayload {
        require(payload.size >= HEADER_SIZE) {
            "Payload too short (${payload.size} bytes) — corrupt image?"
        }

        val buf     = ByteBuffer.wrap(payload)
        val magic   = ByteArray(4).also { buf.get(it) }
        require(magic.contentEquals(SignatureDetector.STEALTH_MAGIC)) {
            "Invalid HXGS magic — this may not be a HexGlyph Stealth image."
        }

        val version = buf.get()
        require(version == StealthEncoder.VERSION) { "Unsupported stealth version: $version" }

        val flags          = buf.get().toInt()
        val groupProtected = (flags and StealthEncoder.FLAG_GROUP_PROTECTED.toInt()) != 0

        val payloadLen  = buf.int
        val storedCrc   = buf.int

        require(payloadLen > 0 && buf.remaining() >= payloadLen) {
            "Corrupt payloadLen: $payloadLen (remaining: ${buf.remaining()})"
        }

        val encryptedBlock = ByteArray(payloadLen).also { buf.get(it) }

        // Validate checksum
        val actualCrc = CRC32().apply { update(encryptedBlock) }.value.toInt()
        require(actualCrc == storedCrc) {
            "Checksum mismatch — image may have been compressed or tampered."
        }

        return ParsedPayload(groupProtected, encryptedBlock)
    }

    // ── Group Protection removal ──────────────────────────────────────────────
    // Mirrors StealthEncoder.applyGroupProtection exactly:
    // wire format: [12-byte IV] [AES-GCM ciphertext + 16-byte tag]

    private fun stripGroupProtection(block: ByteArray, groupCode: String): ByteArray {
        require(block.size > 12 + 16) {
            "Group-protected block too short (${block.size} bytes)"
        }
        val key        = MessageDigest.getInstance("SHA-256")
            .digest(groupCode.toByteArray(Charsets.UTF_8))
        val iv         = block.copyOf(12)
        val ciphertext = block.copyOfRange(12, block.size)
        val cipher     = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, iv))
        return cipher.doFinal(ciphertext)
    }

    // ── Seed derivation (must mirror StealthEncoder exactly) ─────────────────
    // Uses bits 23-17 of the R channel (ushr 17 & 0x7F) — the 7 high bits of R.
    // Single-LSB embedding only ever touches bit 0 of each channel (stored at
    // bit 16 in an ARGB_8888 int for R). Bits 23-17 are NEVER modified, so the
    // fingerprint is identical before and after any LSB embedding pass.

    private fun deriveEmbeddingSeed(groupCode: String, bitmap: Bitmap): ByteArray {
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
