package com.hexglyph.crypto

/**
 * Pure-Kotlin ChaCha20 stream cipher (RFC 7539).
 *
 * Usage:
 *   val keystream = ChaCha20.keystream(key32, nonce12, counter = 0, length)
 *   val encrypted = ChaCha20.xor(plaintext, key32, nonce12)
 */
object ChaCha20 {

    // ── Core quarter-round ───────────────────────────────────────────────────

    private fun quarterRound(state: IntArray, a: Int, b: Int, c: Int, d: Int) {
        state[a] += state[b]; state[d] = Integer.rotateLeft(state[d] xor state[a], 16)
        state[c] += state[d]; state[b] = Integer.rotateLeft(state[b] xor state[c], 12)
        state[a] += state[b]; state[d] = Integer.rotateLeft(state[d] xor state[a],  8)
        state[c] += state[d]; state[b] = Integer.rotateLeft(state[b] xor state[c],  7)
    }

    // ── Block function ───────────────────────────────────────────────────────

    private fun block(key: ByteArray, nonce: ByteArray, counter: Int): ByteArray {
        require(key.size == 32)   { "ChaCha20: key must be 32 bytes" }
        require(nonce.size == 12) { "ChaCha20: nonce must be 12 bytes" }

        val state = IntArray(16)
        // Constants "expand 32-byte k"
        state[0]  = 0x61707865; state[1] = 0x3320646e
        state[2]  = 0x79622d32; state[3] = 0x6b206574
        // Key (little-endian)
        for (i in 0..7) state[4 + i] = key.leInt(i * 4)
        // Counter + nonce
        state[12] = counter
        state[13] = nonce.leInt(0)
        state[14] = nonce.leInt(4)
        state[15] = nonce.leInt(8)

        val working = state.copyOf()
        repeat(10) { // 20 rounds = 10 double-rounds
            quarterRound(working, 0, 4,  8, 12)
            quarterRound(working, 1, 5,  9, 13)
            quarterRound(working, 2, 6, 10, 14)
            quarterRound(working, 3, 7, 11, 15)
            quarterRound(working, 0, 5, 10, 15)
            quarterRound(working, 1, 6, 11, 12)
            quarterRound(working, 2, 7,  8, 13)
            quarterRound(working, 3, 4,  9, 14)
        }

        val out = ByteArray(64)
        for (i in 0..15) {
            val word = working[i] + state[i]
            out[i * 4 + 0] = (word        ).toByte()
            out[i * 4 + 1] = (word ushr  8).toByte()
            out[i * 4 + 2] = (word ushr 16).toByte()
            out[i * 4 + 3] = (word ushr 24).toByte()
        }
        return out
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Generate [length] bytes of keystream.
     */
    fun keystream(key: ByteArray, nonce: ByteArray, counter: Int = 1, length: Int): ByteArray {
        val out    = ByteArray(length)
        var offset = 0
        var ctr    = counter
        while (offset < length) {
            val blk  = block(key, nonce, ctr++)
            val copy = minOf(64, length - offset)
            System.arraycopy(blk, 0, out, offset, copy)
            offset += copy
        }
        return out
    }

    /**
     * XOR [data] with ChaCha20 keystream — encryption = decryption.
     */
    fun xor(data: ByteArray, key: ByteArray, nonce: ByteArray, counter: Int = 1): ByteArray {
        val ks  = keystream(key, nonce, counter, data.size)
        return ByteArray(data.size) { i -> (data[i].toInt() xor ks[i].toInt()).toByte() }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun ByteArray.leInt(offset: Int): Int =
        (this[offset    ].toInt() and 0xFF)        or
        ((this[offset + 1].toInt() and 0xFF) shl  8) or
        ((this[offset + 2].toInt() and 0xFF) shl 16) or
        ((this[offset + 3].toInt() and 0xFF) shl 24)
}
