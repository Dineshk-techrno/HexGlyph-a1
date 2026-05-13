package com.hexglyph.scanner

import android.graphics.Bitmap
import android.graphics.PointF
import com.hexglyph.renderer.HexMath
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * BlobDetector — finds the 3 orange anchor cells in a scanned glyph bitmap.
 *
 * Pipeline:
 *   1. Binarise each pixel: luminance > 128 → white (1), else dark (0)
 *   2. Find orange blobs: pixels close to anchor colour #FF6B35
 *   3. Group connected pixels via flood-fill → blob centroids
 *   4. Filter by area & circularity
 *   5. Return the 3 largest/most-circular blobs as anchor candidates
 */
object BlobDetector {

    // Anchor colour #FF6B35
    private const val ANCHOR_R = 0xFF
    private const val ANCHOR_G = 0x6B
    private const val ANCHOR_B = 0x35
    private const val COLOR_THRESHOLD = 60  // per-channel tolerance

    private const val MIN_BLOB_AREA      = 20
    private const val MAX_BLOB_AREA      = 50_000
    private const val MIN_CIRCULARITY    = 0.3f

    data class Blob(
        val centroid: PointF,
        val area: Int,
        val circularity: Float
    )

    /**
     * Detect anchor blobs in [bitmap].
     * @return list of up to 3 blob centroids (PointF in bitmap coordinates)
     */
    fun detectAnchors(bitmap: Bitmap): List<PointF> {
        val scaled = scaleBitmap(bitmap, 1024)
        val width  = scaled.width
        val height = scaled.height

        // Extract pixel array
        val pixels = IntArray(width * height)
        scaled.getPixels(pixels, 0, width, 0, 0, width, height)

        val visited = BooleanArray(pixels.size)
        val blobs   = mutableListOf<Blob>()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val idx = y * width + x
                if (!visited[idx] && isOrangePixel(pixels[idx])) {
                    val blobPixels = floodFill(pixels, visited, x, y, width, height)
                    if (blobPixels.size in MIN_BLOB_AREA..MAX_BLOB_AREA) {
                        val centroid     = centroid(blobPixels)
                        val circularity  = circularity(blobPixels)
                        if (circularity >= MIN_CIRCULARITY) {
                            blobs.add(Blob(centroid, blobPixels.size, circularity))
                        }
                    }
                }
            }
        }

        // Scale centroids back to original bitmap coordinates
        val scaleX = bitmap.width.toFloat()  / scaled.width
        val scaleY = bitmap.height.toFloat() / scaled.height

        return blobs
            .sortedByDescending { it.circularity * it.area }
            .take(HexMath.ANCHOR_COUNT)
            .map { PointF(it.centroid.x * scaleX, it.centroid.y * scaleY) }
    }

    // ── Flood fill ───────────────────────────────────────────────────────────

    private fun floodFill(
        pixels: IntArray, visited: BooleanArray,
        startX: Int, startY: Int, width: Int, height: Int
    ): List<Pair<Int, Int>> {
        val result  = mutableListOf<Pair<Int, Int>>()
        val queue   = ArrayDeque<Pair<Int, Int>>()
        queue.add(startX to startY)

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            val idx    = y * width + x
            if (x < 0 || x >= width || y < 0 || y >= height) continue
            if (visited[idx] || !isOrangePixel(pixels[idx]))   continue
            visited[idx] = true
            result.add(x to y)
            queue.add((x + 1) to y)
            queue.add((x - 1) to y)
            queue.add(x to (y + 1))
            queue.add(x to (y - 1))
        }
        return result
    }

    // ── Geometry helpers ─────────────────────────────────────────────────────

    private fun centroid(pixels: List<Pair<Int, Int>>): PointF {
        val sumX = pixels.sumOf { it.first }.toFloat()
        val sumY = pixels.sumOf { it.second }.toFloat()
        return PointF(sumX / pixels.size, sumY / pixels.size)
    }

    private fun circularity(pixels: List<Pair<Int, Int>>): Float {
        val area      = pixels.size.toFloat()
        val cx        = pixels.sumOf { it.first }.toFloat()  / area
        val cy        = pixels.sumOf { it.second }.toFloat() / area
        val radius    = sqrt(area / Math.PI).toFloat()
        val perimEst  = 2f * Math.PI.toFloat() * radius
        // Simplified: actual perimeter estimated from bounding box ratio
        val minX = pixels.minOf { it.first }.toFloat()
        val maxX = pixels.maxOf { it.first }.toFloat()
        val minY = pixels.minOf { it.second }.toFloat()
        val maxY = pixels.maxOf { it.second }.toFloat()
        val bboxPerim = 2f * ((maxX - minX) + (maxY - minY))
        return if (bboxPerim > 0) perimEst / bboxPerim else 0f
    }

    // ── Colour filter ────────────────────────────────────────────────────────

    private fun isOrangePixel(pixel: Int): Boolean {
        val r = (pixel ushr 16) and 0xFF
        val g = (pixel ushr  8) and 0xFF
        val b =  pixel          and 0xFF
        return abs(r - ANCHOR_R) < COLOR_THRESHOLD &&
               abs(g - ANCHOR_G) < COLOR_THRESHOLD &&
               abs(b - ANCHOR_B) < COLOR_THRESHOLD
    }

    // ── Scale helper ─────────────────────────────────────────────────────────

    private fun scaleBitmap(bitmap: Bitmap, minDim: Int): Bitmap {
        if (bitmap.width >= minDim && bitmap.height >= minDim) return bitmap
        val scale  = minDim.toFloat() / minOf(bitmap.width, bitmap.height)
        val newW   = (bitmap.width  * scale).toInt()
        val newH   = (bitmap.height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, newW, newH, true)
    }
}
