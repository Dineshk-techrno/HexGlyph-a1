package com.hexglyph.steganography

import android.graphics.Bitmap
import android.graphics.Color

/**
 * RegionAnalyzer
 *
 * Analyses a carrier image and scores each pixel by its local texture
 * complexity using a simplified Sobel edge detector applied to luminance.
 *
 * High-complexity pixels (edges, shadows, metallic surfaces, noisy textures)
 * are preferred for embedding because a 1-LSB change is statistically
 * invisible among natural variation.
 *
 * Low-complexity pixels (smooth gradients, flat white/clean surfaces) are
 * avoided — they are the most likely to reveal artefacts to statistical
 * steganalysis tools.
 *
 * Usage:
 *   val mask = RegionAnalyzer.buildEmbeddingMask(bitmap, threshold = 10)
 *   // mask[y * width + x] == true  →  safe to embed
 */
object RegionAnalyzer {

    /**
     * Build a boolean mask of pixels suitable for steganographic embedding.
     *
     * [threshold] — minimum Sobel gradient magnitude (0–255) required for
     * a pixel to be considered a "textured" region. Default 8 is conservative
     * but effective; raise it to be more selective.
     */
    fun buildEmbeddingMask(bitmap: Bitmap, threshold: Int = 8): BooleanArray {
        val w      = bitmap.width
        val h      = bitmap.height
        val pixels = IntArray(w * h).also { bitmap.getPixels(it, 0, w, 0, 0, w, h) }
        val luma   = FloatArray(w * h) { i ->
            val c = pixels[i]
            // BT.601 luminance
            0.299f * Color.red(c) + 0.587f * Color.green(c) + 0.114f * Color.blue(c)
        }

        val mask = BooleanArray(w * h) { false }

        for (y in 1 until h - 1) {
            for (x in 1 until w - 1) {
                // 3×3 Sobel kernels
                val gx = (-luma[(y - 1) * w + (x - 1)]
                        - 2 * luma[y * w + (x - 1)]
                        - luma[(y + 1) * w + (x - 1)]
                        + luma[(y - 1) * w + (x + 1)]
                        + 2 * luma[y * w + (x + 1)]
                        + luma[(y + 1) * w + (x + 1)])

                val gy = (-luma[(y - 1) * w + (x - 1)]
                        - 2 * luma[(y - 1) * w + x]
                        - luma[(y - 1) * w + (x + 1)]
                        + luma[(y + 1) * w + (x - 1)]
                        + 2 * luma[(y + 1) * w + x]
                        + luma[(y + 1) * w + (x + 1)])

                val magnitude = Math.sqrt((gx * gx + gy * gy).toDouble()).toFloat()
                mask[y * w + x] = magnitude >= threshold
            }
        }

        // Edge pixels (border row/column) default to false — never embed there.
        return mask
    }

    /**
     * Returns the fraction of pixels in [mask] that are suitable for
     * embedding. Used to decide if the carrier has sufficient capacity.
     */
    fun coverageFraction(mask: BooleanArray): Float =
        mask.count { it }.toFloat() / mask.size

    /**
     * Returns the maximum byte capacity of a bitmap given [mask].
     * Each suitable pixel offers 3 bits (R, G, B LSB); 8 bits = 1 byte.
     */
    fun maxPayloadBytes(mask: BooleanArray): Int =
        (mask.count { it } * 3) / 8
}
