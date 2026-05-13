package com.hexglyph.steganography

import java.security.MessageDigest

/**
 * Deterministic pseudo-random pixel mapper for steganographic embedding.
 *
 * Given a seed derived from the payload key, produces a non-repeating,
 * non-sequential stream of (x, y, channel) triples covering the entire
 * bitmap capacity. Uses a simple LCG seeded from SHA-256(seed) — same
 * approach as KeyDerivation.SeededRng so behaviour is consistent across
 * the codebase.
 *
 * Channel mapping:  0 → R,  1 → G,  2 → B
 */
class RandomPixelMapper(seed: ByteArray) {

    private val rng: SeededRng

    init {
        val digest = MessageDigest.getInstance("SHA-256").digest(seed)
        rng = SeededRng(digest)
    }

    /**
     * Build a shuffled index list of every (pixelIndex * 3 + channel) slot
     * within a bitmap of [width]×[height] pixels.
     *
     * Returns a list of [SlotAddress] in randomised order. Callers iterate
     * through the list to assign one payload bit per slot.
     */
    fun buildSlotSequence(width: Int, height: Int): List<SlotAddress> {
        val total = width * height * 3          // R,G,B per pixel
        val indices = IntArray(total) { it }

        // Fisher-Yates shuffle using the seeded RNG
        for (i in total - 1 downTo 1) {
            val j = rng.nextInt(i + 1)
            val tmp = indices[i]; indices[i] = indices[j]; indices[j] = tmp
        }

        return indices.map { slot ->
            val channel    = slot % 3
            val pixelIndex = slot / 3
            val x          = pixelIndex % width
            val y          = pixelIndex / width
            SlotAddress(x, y, channel)
        }
    }

    // ── Data class ────────────────────────────────────────────────────────────

    data class SlotAddress(val x: Int, val y: Int, val channel: Int)

    // ── LCG — mirrors KeyDerivation.SeededRng ─────────────────────────────────

    private class SeededRng(seed: ByteArray) {
        private var state: Long =
            seed.fold(0L) { acc, b -> acc * 31L + (b.toLong() and 0xFF) }

        fun nextInt(bound: Int): Int {
            state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
            return ((state ushr 17) % bound.toLong()).toInt()
        }
    }
}
