package com.hexglyph.steganography

import android.graphics.Bitmap

/**
 * SignatureDetector
 *
 * Reads the first N bits from a carrier image (using the deterministic
 * slot sequence for a well-known seed) to check whether a hidden
 * HexGlyph Stealth payload is present.
 *
 * The signature bytes "HXGS" (0x48 0x58 0x47 0x53) are embedded at the
 * very start of the bit stream — before any version/flags/length data —
 * so detection is fast and requires no group code.
 */
object SignatureDetector {

    /** ASCII bytes for "HXGS" */
    val STEALTH_MAGIC = byteArrayOf(0x48, 0x58, 0x47, 0x53)

    private const val SIGNATURE_SEED_CONSTANT = "HXGS-SIGNATURE-SEED-V1"

    /**
     * Returns true when [bitmap] contains the hidden HXGS signature,
     * indicating that it carries a Stealth Glyph payload.
     */
    fun hasSignature(bitmap: Bitmap): Boolean {
        return try {
            val seed  = SIGNATURE_SEED_CONSTANT.toByteArray(Charsets.UTF_8)
            val mapper = RandomPixelMapper(seed)
            val slots  = mapper.buildSlotSequence(bitmap.width, bitmap.height)
            val reader = BitStreamReader(bitmap, slots)
            val magic  = reader.readBytes(4)
            magic.contentEquals(STEALTH_MAGIC)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Embeds the HXGS signature into [bitmap] at the canonical signature
     * slot positions. Called by [StealthEncoder] before writing payload data.
     */
    fun embedSignature(bitmap: Bitmap) {
        val seed   = SIGNATURE_SEED_CONSTANT.toByteArray(Charsets.UTF_8)
        val mapper = RandomPixelMapper(seed)
        val slots  = mapper.buildSlotSequence(bitmap.width, bitmap.height)
        val writer = BitStreamWriter(bitmap, slots)
        writer.writeBytes(STEALTH_MAGIC)
    }
}
