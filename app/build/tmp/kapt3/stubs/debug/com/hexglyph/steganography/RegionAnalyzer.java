package com.hexglyph.steganography;

import android.graphics.Bitmap;
import android.graphics.Color;

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
 *  val mask = RegionAnalyzer.buildEmbeddingMask(bitmap, threshold = 10)
 *  // mask[y * width + x] == true  →  safe to embed
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0018\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0004\u00a8\u0006\r"}, d2 = {"Lcom/hexglyph/steganography/RegionAnalyzer;", "", "()V", "buildEmbeddingMask", "", "bitmap", "Landroid/graphics/Bitmap;", "threshold", "", "coverageFraction", "", "mask", "maxPayloadBytes", "app_debug"})
public final class RegionAnalyzer {
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.steganography.RegionAnalyzer INSTANCE = null;
    
    private RegionAnalyzer() {
        super();
    }
    
    /**
     * Build a boolean mask of pixels suitable for steganographic embedding.
     *
     * [threshold] — minimum Sobel gradient magnitude (0–255) required for
     * a pixel to be considered a "textured" region. Default 8 is conservative
     * but effective; raise it to be more selective.
     */
    @org.jetbrains.annotations.NotNull()
    public final boolean[] buildEmbeddingMask(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, int threshold) {
        return null;
    }
    
    /**
     * Returns the fraction of pixels in [mask] that are suitable for
     * embedding. Used to decide if the carrier has sufficient capacity.
     */
    public final float coverageFraction(@org.jetbrains.annotations.NotNull()
    boolean[] mask) {
        return 0.0F;
    }
    
    /**
     * Returns the maximum byte capacity of a bitmap given [mask].
     * Each suitable pixel offers 3 bits (R, G, B LSB); 8 bits = 1 byte.
     */
    public final int maxPayloadBytes(@org.jetbrains.annotations.NotNull()
    boolean[] mask) {
        return 0;
    }
}