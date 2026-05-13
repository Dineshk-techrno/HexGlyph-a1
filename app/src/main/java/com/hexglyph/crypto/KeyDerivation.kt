package com.hexglyph.crypto

import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Key derivation utilities:
 *  - HKDF-SHA-256 producing K1 (32 bytes) and K2 (32 bytes)
 *  - Daily epoch (UTC midnight counter)
 *  - Fisher-Yates shuffle seeded from SHA-256(K1)
 */
object KeyDerivation {

    private const val HMAC_SHA256 = "HmacSHA256"
    private const val SHA256      = "SHA-256"

    // ── HKDF ────────────────────────────────────────────────────────────────

    /**
     * HKDF-Extract: PRK = HMAC-SHA256(salt, ikm)
     */
    private fun hkdfExtract(salt: ByteArray, ikm: ByteArray): ByteArray {
        val mac = Mac.getInstance(HMAC_SHA256)
        mac.init(SecretKeySpec(salt, HMAC_SHA256))
        return mac.doFinal(ikm)
    }

    /**
     * HKDF-Expand: produces [length] bytes of keying material from PRK + info.
     */
    private fun hkdfExpand(prk: ByteArray, info: ByteArray, length: Int): ByteArray {
        val mac  = Mac.getInstance(HMAC_SHA256)
        val out  = ByteArray(length)
        var prev = ByteArray(0)
        var pos  = 0
        var n    = 1
        while (pos < length) {
            mac.init(SecretKeySpec(prk, HMAC_SHA256))
            mac.update(prev)
            mac.update(info)
            mac.update(n.toByte())
            prev = mac.doFinal()
            val copy = minOf(prev.size, length - pos)
            System.arraycopy(prev, 0, out, pos, copy)
            pos += copy
            n++
        }
        return out
    }

    /**
     * Derive K1 and K2 from groupCode + epochDay, salted with gcmIv.
     *
     * ikm  = groupCode.toByteArray(UTF-8) + epochDay (8-byte big-endian long)
     * salt = gcmIv (12 bytes)
     * OKM  = 64 bytes → K1 = [0..31], K2 = [32..63]
     */
    fun deriveKeys(groupCode: String, gcmIv: ByteArray): Pair<ByteArray, ByteArray> {
        val epochDay = currentEpochDay()
        val ikm = groupCode.toByteArray(Charsets.UTF_8) + epochDay.toBytesBigEndian()
        val prk = hkdfExtract(salt = gcmIv, ikm = ikm)
        val okm = hkdfExpand(prk, info = "hexglyph-v1".toByteArray(), length = 64)
        val k1 = okm.copyOfRange(0, 32)
        val k2 = okm.copyOfRange(32, 64)
        return k1 to k2
    }

    // ── Epoch ────────────────────────────────────────────────────────────────

    /** UTC days since epoch (rotates at 00:00:00 UTC). */
    fun currentEpochDay(): Long =
        Instant.now().atOffset(ZoneOffset.UTC)
            .toLocalDate()
            .toEpochDay()

    private fun Long.toBytesBigEndian(): ByteArray = ByteArray(8) { i ->
        ((this shr (56 - i * 8)) and 0xFF).toByte()
    }

    // ── SHA-256 ──────────────────────────────────────────────────────────────

    fun sha256(data: ByteArray): ByteArray =
        MessageDigest.getInstance(SHA256).digest(data)

    fun sha256(vararg parts: ByteArray): ByteArray {
        val md = MessageDigest.getInstance(SHA256)
        parts.forEach { md.update(it) }
        return md.digest()
    }

    // ── Fisher-Yates shuffle ─────────────────────────────────────────────────

    /**
     * Deterministic Fisher-Yates shuffle of [data] in-place.
     * Seed = SHA-256(K1); uses a simple LCG seeded from the hash bytes.
     */
    fun shuffleBytes(data: ByteArray, k1: ByteArray): ByteArray {
        val copy  = data.copyOf()
        val seed  = sha256(k1)
        val rng   = SeededRng(seed)
        for (i in copy.indices.reversed()) {
            val j = rng.nextInt(i + 1)
            val tmp = copy[i]; copy[i] = copy[j]; copy[j] = tmp
        }
        return copy
    }

    /**
     * Reverse Fisher-Yates: reconstruct original order from shuffled [data].
     */
    fun unshuffleBytes(data: ByteArray, k1: ByteArray): ByteArray {
        val copy   = data.copyOf()
        val seed   = sha256(k1)
        val rng    = SeededRng(seed)
        // Replay indices
        val indices = IntArray(copy.size) { i ->
            rng.nextInt(i + 1)
        }
        // Reverse the swaps
        for (i in copy.indices) {
            val j = indices[i]
            val tmp = copy[i]; copy[i] = copy[j]; copy[j] = tmp
        }
        // Reverse the traversal order
        for (i in 0 until copy.size / 2) {
            val j = copy.size - 1 - i
            val tmp = copy[i]; copy[i] = copy[j]; copy[j] = tmp
        }
        return copy
    }

    // ── Seeded RNG (LCG) ─────────────────────────────────────────────────────

    private class SeededRng(seed: ByteArray) {
        private var state: Long = seed.fold(0L) { acc, b -> acc * 31 + b.toLong() }

        fun nextInt(bound: Int): Int {
            state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
            return ((state ushr 17) % bound).toInt()
        }
    }
}
