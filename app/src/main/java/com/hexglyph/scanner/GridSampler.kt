package com.hexglyph.scanner

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import com.hexglyph.renderer.HexMath
import kotlin.math.roundToInt

/**
 * GridSampler — samples each hex cell centre through an affine transform
 * and returns a bit array for the CryptoEngine.
 *
 * Input: bitmap + 3 detected anchor centroids (from BlobDetector)
 * Output: BooleanArray of DATA_CELLS bits
 */
object GridSampler {

    private const val LUMINANCE_THRESHOLD = 128

    /**
     * Sample the glyph grid from [bitmap], using [detectedAnchors] to compute
     * the affine correction that maps scanned anchor positions to ideal grid positions.
     *
     * @return BooleanArray[DATA_CELLS] — true=lit (bit 1), false=dark (bit 0)
     */
    fun sample(bitmap: Bitmap, detectedAnchors: List<PointF>, hexMath: HexMath): BooleanArray {
        require(detectedAnchors.size == HexMath.ANCHOR_COUNT) {
            "Need exactly ${HexMath.ANCHOR_COUNT} anchor points, got ${detectedAnchors.size}"
        }

        // Ideal anchor positions in glyph coordinate space (centre = 0,0)
        val cellSize   = hexMath.optimalCellSize(maxOf(bitmap.width, bitmap.height))
        val idealAnchors = hexMath.anchorCells().map { (q, r) ->
            val pt = hexMath.axialToPixel(q, r, cellSize)
            PointF(bitmap.width / 2f + pt.x, bitmap.height / 2f + pt.y)
        }

        // Compute affine transform: detected → ideal (for mapping ideal→scan coords)
        val transform = computeAffine(idealAnchors, detectedAnchors)

        // Precompute spiral cells and anchor set
        val spiralCells = hexMath.spiralCells()
        val anchorSet   = hexMath.anchorCells().toSet()

        val bits    = BooleanArray(HexMath.DATA_CELLS)
        var bitIdx  = 0

        for ((q, r) in spiralCells) {
            if (Pair(q, r) in anchorSet) continue  // skip anchor cells

            // Ideal cell centre
            val ideal = hexMath.axialToPixel(q, r, cellSize)
            val idealX = bitmap.width / 2f + ideal.x
            val idealY = bitmap.height / 2f + ideal.y

            // Map ideal → scanned image coordinates
            val scanPt = applyAffine(transform, PointF(idealX, idealY))

            // Sample pixel and binarise by luminance
            val lum = sampleLuminance(bitmap, scanPt.x.roundToInt(), scanPt.y.roundToInt())
            bits[bitIdx++] = lum > LUMINANCE_THRESHOLD

            if (bitIdx >= HexMath.DATA_CELLS) break
        }

        return bits
    }

    // ── Affine transform ─────────────────────────────────────────────────────

    /**
     * Compute a 2D affine matrix mapping [src] points to [dst] points.
     * Uses Android's Matrix with 3-point setPolyToPoly.
     */
    private fun computeAffine(src: List<PointF>, dst: List<PointF>): Matrix {
        val srcArr = floatArrayOf(src[0].x, src[0].y, src[1].x, src[1].y, src[2].x, src[2].y)
        val dstArr = floatArrayOf(dst[0].x, dst[0].y, dst[1].x, dst[1].y, dst[2].x, dst[2].y)
        val m      = Matrix()
        m.setPolyToPoly(srcArr, 0, dstArr, 0, 3)
        return m
    }

    private fun applyAffine(matrix: Matrix, point: PointF): PointF {
        val pts = floatArrayOf(point.x, point.y)
        matrix.mapPoints(pts)
        return PointF(pts[0], pts[1])
    }

    // ── Pixel sampling ───────────────────────────────────────────────────────

    private fun sampleLuminance(bitmap: Bitmap, x: Int, y: Int): Int {
        val cx = x.coerceIn(0, bitmap.width  - 1)
        val cy = y.coerceIn(0, bitmap.height - 1)
        val px = bitmap.getPixel(cx, cy)
        val r  = (px ushr 16) and 0xFF
        val g  = (px ushr  8) and 0xFF
        val b  =  px          and 0xFF
        // BT.601 luminance
        return (0.299f * r + 0.587f * g + 0.114f * b).roundToInt()
    }
}
