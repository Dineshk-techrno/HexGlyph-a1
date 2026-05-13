package com.hexglyph.crypto

/**
 * Post-RS obfuscation layer (Obfuscator.kt).
 *
 * NOT a cryptographic primitive — AES-GCM already handles auth + confidentiality.
 * Purpose: make the RS-encoded block visually unpredictable, preventing simple
 * byte-frequency analysis of raw glyph pixels.
 *
 * Algorithm: 3-round XOR+rotate
 *   for round in 0..2:
 *     roundKey = SHA-256(K1 + "hexglyph-obf-round-N")  // 32 bytes
 *     for each byte:
 *       rotated = rotateLeft(byte, 1)
 *       result  = rotated XOR roundKey[i % 32]
 *
 * Decode: rounds 2→1→0 reversed (unXOR then rotateRight).
 */
object Obfuscator {

    fun obfuscate(data: ByteArray, k1: ByteArray): ByteArray {
        var current = data.copyOf()
        for (round in 0..2) {
            val roundKey = roundKey(k1, round)
            current = ByteArray(current.size) { i ->
                val rotated = rotateLeft(current[i])
                (rotated.toInt() xor (roundKey[i % 32].toInt() and 0xFF)).toByte()
            }
        }
        return current
    }

    fun deobfuscate(data: ByteArray, k1: ByteArray): ByteArray {
        var current = data.copyOf()
        for (round in 2 downTo 0) {
            val roundKey = roundKey(k1, round)
            current = ByteArray(current.size) { i ->
                val unXored = (current[i].toInt() xor (roundKey[i % 32].toInt() and 0xFF)).toByte()
                rotateRight(unXored)
            }
        }
        return current
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun roundKey(k1: ByteArray, round: Int): ByteArray =
        KeyDerivation.sha256(k1, "hexglyph-obf-round-$round".toByteArray(Charsets.UTF_8))

    private fun rotateLeft(b: Byte): Byte {
        val v = b.toInt() and 0xFF
        return ((v shl 1) or (v ushr 7)).toByte()
    }

    private fun rotateRight(b: Byte): Byte {
        val v = b.toInt() and 0xFF
        return ((v ushr 1) or (v shl 7)).toByte()
    }
}
