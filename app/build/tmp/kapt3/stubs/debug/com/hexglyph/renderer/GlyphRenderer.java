package com.hexglyph.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import javax.inject.Inject;

/**
 * GlyphRenderer — converts a [DATA_CELLS]-bit payload into a square Bitmap.
 *
 * Output: typically 1,400–1,500 px/side at 1× density.
 * Each hex cell is filled based on its bit value (lit=white, dark=near-black).
 * Anchor cells are painted orange regardless of data.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0018\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 (2\u00020\u0001:\u0001(B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u0018J0\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020\u0006H\u0002J\u000e\u0010&\u001a\u00020\'2\u0006\u0010\u001c\u001a\u00020\u0018R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R-\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\t0\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R-\u0010\u0012\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\t0\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u000e\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006)"}, d2 = {"Lcom/hexglyph/renderer/GlyphRenderer;", "", "hexMath", "Lcom/hexglyph/renderer/HexMath;", "(Lcom/hexglyph/renderer/HexMath;)V", "anchorPaint", "Landroid/graphics/Paint;", "anchorSet", "", "Lkotlin/Pair;", "", "getAnchorSet", "()Ljava/util/Set;", "anchorSet$delegate", "Lkotlin/Lazy;", "bgPaint", "darkPaint", "litPaint", "spiralCells", "", "getSpiralCells", "()Ljava/util/List;", "spiralCells$delegate", "bitsToBytes", "", "bits", "", "bytesToBits", "dataBytes", "drawHexagon", "", "canvas", "Landroid/graphics/Canvas;", "cx", "", "cy", "size", "paint", "render", "Landroid/graphics/Bitmap;", "Companion", "app_debug"})
public final class GlyphRenderer {
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.renderer.HexMath hexMath = null;
    public static final int BITMAP_SIZE = 1440;
    public static final float CELL_GAP_RATIO = 0.05F;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint bgPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint litPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint darkPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint anchorPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy spiralCells$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy anchorSet$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.renderer.GlyphRenderer.Companion Companion = null;
    
    @javax.inject.Inject()
    public GlyphRenderer(@org.jetbrains.annotations.NotNull()
    com.hexglyph.renderer.HexMath hexMath) {
        super();
    }
    
    private final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> getSpiralCells() {
        return null;
    }
    
    private final java.util.Set<kotlin.Pair<java.lang.Integer, java.lang.Integer>> getAnchorSet() {
        return null;
    }
    
    /**
     * Render the glyph from [dataBytes] (raw 305-byte payload from CryptoEngine).
     * @return square Bitmap (ARGB_8888)
     */
    @org.jetbrains.annotations.NotNull()
    public final android.graphics.Bitmap render(@org.jetbrains.annotations.NotNull()
    byte[] dataBytes) {
        return null;
    }
    
    /**
     * Build a bit array from [dataBytes] that can be stored back in the glyph cells.
     * Anchor cells are skipped (they carry no data bits).
     */
    @org.jetbrains.annotations.NotNull()
    public final boolean[] bytesToBits(@org.jetbrains.annotations.NotNull()
    byte[] dataBytes) {
        return null;
    }
    
    /**
     * Reconstruct bytes from a bit array read back from the scanner.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] bitsToBytes(@org.jetbrains.annotations.NotNull()
    boolean[] bits) {
        return null;
    }
    
    private final void drawHexagon(android.graphics.Canvas canvas, float cx, float cy, float size, android.graphics.Paint paint) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/hexglyph/renderer/GlyphRenderer$Companion;", "", "()V", "BITMAP_SIZE", "", "CELL_GAP_RATIO", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}