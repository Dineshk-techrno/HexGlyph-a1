package com.hexglyph.renderer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import javax.inject.Inject

/**
 * GlyphRenderer — converts a [DATA_CELLS]-bit payload into a square Bitmap.
 *
 * Output: typically 1,400–1,500 px/side at 1× density.
 * Each hex cell is filled based on its bit value (lit=white, dark=near-black).
 * Anchor cells are painted orange regardless of data.
 */
class GlyphRenderer @Inject constructor(private val hexMath: HexMath) {

    companion object {
        const val BITMAP_SIZE    = 1440   // px, square output
        const val CELL_GAP_RATIO = 0.05f  // fractional gap between cells
    }

    private val bgPaint     = Paint().apply { color = HexMath.COLOR_BACKGROUND; style = Paint.Style.FILL }
    private val litPaint    = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = HexMath.COLOR_LIT;    style = Paint.Style.FILL }
    private val darkPaint   = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = HexMath.COLOR_DARK;   style = Paint.Style.FILL }
    private val anchorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = HexMath.COLOR_ANCHOR; style = Paint.Style.FILL }

    // Precompute spiral + anchor sets
    private val spiralCells by lazy { hexMath.spiralCells() }
    private val anchorSet   by lazy { hexMath.anchorCells().toSet() }

    /**
     * Render the glyph from [dataBytes] (raw 305-byte payload from CryptoEngine).
     * @return square Bitmap (ARGB_8888)
     */
    fun render(dataBytes: ByteArray): Bitmap {
        require(dataBytes.size >= HexMath.DATA_CAPACITY_BYTES) {
            "dataBytes too short: ${dataBytes.size}, need ${HexMath.DATA_CAPACITY_BYTES}"
        }

        val bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRect(0f, 0f, BITMAP_SIZE.toFloat(), BITMAP_SIZE.toFloat(), bgPaint)

        val cx       = BITMAP_SIZE / 2f
        val cy       = BITMAP_SIZE / 2f
        val cellSize = hexMath.optimalCellSize(BITMAP_SIZE)
        val hexSize  = cellSize * (1f - CELL_GAP_RATIO)

        var dataIndex = 0

        for ((q, r) in spiralCells) {
            val centre = hexMath.axialToPixel(q, r, cellSize)
            val px     = cx + centre.x
            val py     = cy + centre.y

            val paint = when {
                Pair(q, r) in anchorSet -> anchorPaint
                else -> {
                    val bitIndex  = dataIndex++
                    val safeIndex = bitIndex / 8
                    // Safe access — last byte holds only 2 used bits; pad remainder with 0
                    val byteVal = if (safeIndex < dataBytes.size)
                        dataBytes[safeIndex].toInt() and 0xFF else 0
                    val bit = (byteVal ushr (7 - bitIndex % 8)) and 1
                    if (bit == 1) litPaint else darkPaint
                }
            }

            drawHexagon(canvas, px, py, hexSize, paint)
        }

        return bitmap
    }

    /**
     * Build a bit array from [dataBytes] that can be stored back in the glyph cells.
     * Anchor cells are skipped (they carry no data bits).
     */
    fun bytesToBits(dataBytes: ByteArray): BooleanArray {
        require(dataBytes.size >= HexMath.DATA_CAPACITY_BYTES) {
            "dataBytes too short: ${dataBytes.size}, need ${HexMath.DATA_CAPACITY_BYTES}"
        }
        val bits = BooleanArray(HexMath.DATA_CELLS)
        for (i in bits.indices) {
            val safeIndex = i / 8
            val byteVal = if (safeIndex < dataBytes.size)
                dataBytes[safeIndex].toInt() and 0xFF else 0
            bits[i] = ((byteVal ushr (7 - i % 8)) and 1) == 1
        }
        return bits
    }

    /**
     * Reconstruct bytes from a bit array read back from the scanner.
     */
    fun bitsToBytes(bits: BooleanArray): ByteArray {
        require(bits.size >= HexMath.DATA_CELLS) {
            "bits array too short: ${bits.size}, need ${HexMath.DATA_CELLS}"
        }
        val bytes = ByteArray(HexMath.DATA_CAPACITY_BYTES)  // fixed: was DATA_CELLS / 8 = 304
        for (i in bytes.indices) {
            var b = 0
            for (j in 0..7) {
                val bitIndex = i * 8 + j
                if (bitIndex < bits.size && bits[bitIndex]) b = b or (1 shl (7 - j))
            }
            bytes[i] = b.toByte()
        }
        return bytes
    }

    // ── Drawing ──────────────────────────────────────────────────────────────

    private fun drawHexagon(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val corners = hexMath.hexCorners(cx, cy, size)
        val path    = Path()
        path.moveTo(corners[0].x, corners[0].y)
        for (i in 1..5) path.lineTo(corners[i].x, corners[i].y)
        path.close()
        canvas.drawPath(path, paint)
    }
}
