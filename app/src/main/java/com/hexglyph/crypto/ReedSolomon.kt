package com.hexglyph.crypto

/**
 * Reed-Solomon error correction over GF(2^8), generator polynomial 0x11D.
 *
 * Parameters (per spec):
 *   ECC symbols  : 10 per 255-byte block
 *   Error capacity: up to 5 byte errors per block
 *   Max data     : 245 bytes per block
 */
object ReedSolomon {

    private const val GEN_POLY     = 0x11D  // x^8 + x^4 + x^3 + x^2 + 1
    private const val FIELD_SIZE   = 256
    private const val ECC_SYMBOLS  = 10
    private const val BLOCK_SIZE   = 255
    private const val MAX_DATA     = BLOCK_SIZE - ECC_SYMBOLS  // 245

    // ── Galois Field tables ──────────────────────────────────────────────────

    private val gfExp = IntArray(FIELD_SIZE * 2)
    private val gfLog = IntArray(FIELD_SIZE)

    init {
        var x = 1
        for (i in 0 until FIELD_SIZE - 1) {
            gfExp[i]             = x
            gfExp[i + FIELD_SIZE - 1] = x
            gfLog[x]             = i
            x = x shl 1
            if (x and FIELD_SIZE != 0) x = x xor GEN_POLY
        }
        gfExp[FIELD_SIZE - 1] = 1
    }

    private fun gfMul(a: Int, b: Int): Int {
        if (a == 0 || b == 0) return 0
        return gfExp[(gfLog[a] + gfLog[b]) % (FIELD_SIZE - 1)]
    }

    private fun gfDiv(a: Int, b: Int): Int {
        require(b != 0) { "RS: division by zero in GF" }
        if (a == 0) return 0
        return gfExp[(gfLog[a] - gfLog[b] + FIELD_SIZE - 1) % (FIELD_SIZE - 1)]
    }

    private fun gfPow(x: Int, power: Int): Int = gfExp[(gfLog[x] * power) % (FIELD_SIZE - 1)]

    private fun gfInverse(x: Int): Int = gfExp[(FIELD_SIZE - 1) - gfLog[x]]

    // ── Generator polynomial ─────────────────────────────────────────────────

    private fun generatorPoly(): IntArray {
        var g = intArrayOf(1)
        for (i in 0 until ECC_SYMBOLS) {
            g = polyMul(g, intArrayOf(1, gfPow(2, i)))
        }
        return g
    }

    private val generator: IntArray by lazy { generatorPoly() }

    private fun polyMul(p: IntArray, q: IntArray): IntArray {
        val result = IntArray(p.size + q.size - 1)
        for (i in p.indices) for (j in q.indices)
            result[i + j] = result[i + j] xor gfMul(p[i], q[j])
        return result
    }

    // ── Encode ───────────────────────────────────────────────────────────────

    /**
     * Encode a full message (arbitrary length) into RS-protected blocks.
     * Returns concatenated [data + ecc] blocks.
     */
    fun encode(data: ByteArray): ByteArray {
        val blocks = data.toList().chunked(MAX_DATA)
        val output = mutableListOf<Byte>()
        for (block in blocks) {
            val blockBytes = block.toByteArray()
            val ecc        = encodeBlock(blockBytes)
            output.addAll(blockBytes.toList())
            output.addAll(ecc.toList())
        }
        return output.toByteArray()
    }

    /**
     * Decode and correct errors in RS-protected data.
     * Returns original data bytes (ECC symbols stripped).
     */
    fun decode(data: ByteArray): ByteArray {
        val blockTotal = MAX_DATA + ECC_SYMBOLS
        val blocks     = data.toList().chunked(blockTotal)
        val output     = mutableListOf<Byte>()
        for (block in blocks) {
            val blockBytes  = block.toByteArray()
            val corrected   = decodeBlock(blockBytes)
            output.addAll(corrected.toList())
        }
        return output.toByteArray()
    }

    // ── Block-level encode ───────────────────────────────────────────────────

    private fun encodeBlock(data: ByteArray): ByteArray {
        require(data.size <= MAX_DATA) { "RS block too large: ${data.size} > $MAX_DATA" }
        val msg    = IntArray(data.size + ECC_SYMBOLS)
        for (i in data.indices) msg[i] = data[i].toInt() and 0xFF

        val gen = generator
        for (i in data.indices) {
            val coef = msg[i]
            if (coef != 0) for (j in gen.indices)
                msg[i + j] = msg[i + j] xor gfMul(gen[j], coef)
        }
        return ByteArray(ECC_SYMBOLS) { i -> msg[data.size + i].toByte() }
    }

    // ── Block-level decode ───────────────────────────────────────────────────

    private fun decodeBlock(block: ByteArray): ByteArray {
        val dataLen = block.size - ECC_SYMBOLS
        val msg     = IntArray(block.size) { i -> block[i].toInt() and 0xFF }

        val syndromes = IntArray(ECC_SYMBOLS) { i ->
            var s = 0
            for (b in msg) s = s xor gfMul(gfPow(2, i), 0).let { gfMul(s, gfPow(2, i)) xor b }
            msg.fold(0) { acc, b -> gfMul(acc, gfPow(2, i)) xor b }
        }

        if (syndromes.all { it == 0 }) {
            return ByteArray(dataLen) { i -> block[i] }
        }

        // Berlekamp-Massey for error locator polynomial
        val errLoc   = berlekampMassey(syndromes)
        val errPos   = findErrors(errLoc, msg.size)
        val corrected = correctErrors(msg, syndromes, errPos)

        return ByteArray(dataLen) { i -> corrected[i].toByte() }
    }

    private fun berlekampMassey(syndromes: IntArray): IntArray {
        var errLoc = intArrayOf(1)
        var oldLoc = intArrayOf(1)
        for (i in syndromes.indices) {
            val delta = syndromes.foldIndexed(syndromes[i]) { j, acc, _ ->
                if (j < errLoc.size - 1) acc xor gfMul(errLoc[errLoc.size - 1 - j - 1], syndromes[i - j - 1])
                else acc
            }
            oldLoc = intArrayOf(0) + oldLoc
            if (delta != 0) {
                val newLoc = IntArray(maxOf(errLoc.size, oldLoc.size))
                val padE   = IntArray(newLoc.size - errLoc.size) + errLoc
                val padO   = IntArray(newLoc.size - oldLoc.size) + oldLoc
                for (j in newLoc.indices)
                    newLoc[j] = padE[j] xor gfMul(delta, padO[j])
                if (2 * (errLoc.size - 1) < i + 1) {
                    errLoc = newLoc
                    oldLoc = padE.map { gfDiv(it, delta) }.toIntArray()
                } else {
                    errLoc = newLoc
                }
            }
        }
        return errLoc
    }

    private fun findErrors(errLoc: IntArray, msgLen: Int): IntArray {
        val errors = mutableListOf<Int>()
        for (i in 0 until msgLen) {
            var res = 0
            for (j in errLoc.indices) res = res xor gfMul(errLoc[errLoc.size - 1 - j], gfPow(2, i * j))
            if (res == 0) errors.add(msgLen - 1 - i)
        }
        return errors.toIntArray()
    }

    private fun correctErrors(msg: IntArray, syndromes: IntArray, errPos: IntArray): IntArray {
        val corrected = msg.copyOf()
        val coefPos   = errPos.map { msg.size - 1 - it }
        // Forney algorithm (simplified)
        val errLocPoly = coefPos.fold(intArrayOf(1)) { acc, root ->
            polyMul(acc, intArrayOf(1, gfPow(2, root)))
        }
        for (i in errPos.indices) {
            val xiInv = gfPow(2, coefPos[i])
            var errLocPrime = 1
            for (j in errPos.indices) {
                if (j != i) errLocPrime = gfMul(errLocPrime, 1 xor gfMul(xiInv, gfPow(2, coefPos[j])))
            }
            val y = gfMul(gfPow(xiInv, 1),
                syndromes.foldIndexed(0) { k, acc, s -> acc xor gfMul(s, gfPow(xiInv, k)) })
            val magnitude = gfDiv(y, errLocPrime)
            corrected[errPos[i]] = corrected[errPos[i]] xor magnitude
        }
        return corrected
    }
}
