package com.hexglyph.scanner;

import android.graphics.Bitmap;
import android.graphics.PointF;
import com.hexglyph.renderer.HexMath;

/**
 * BlobDetector — finds the 3 orange anchor cells in a scanned glyph bitmap.
 *
 * Pipeline:
 *  1. Binarise each pixel: luminance > 128 → white (1), else dark (0)
 *  2. Find orange blobs: pixels close to anchor colour #FF6B35
 *  3. Group connected pixels via flood-fill → blob centroids
 *  4. Filter by area & circularity
 *  5. Return the 3 largest/most-circular blobs as anchor candidates
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0018\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\"B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\f\u001a\u00020\r2\u0018\u0010\u000e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00100\u000fH\u0002J\"\u0010\u0011\u001a\u00020\u000b2\u0018\u0010\u000e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00100\u000fH\u0002J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u000f2\u0006\u0010\u0013\u001a\u00020\u0014JJ\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00100\u000f2\u0006\u0010\u000e\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u0004H\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0004H\u0002J\u0018\u0010 \u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/hexglyph/scanner/BlobDetector;", "", "()V", "ANCHOR_B", "", "ANCHOR_G", "ANCHOR_R", "COLOR_THRESHOLD", "MAX_BLOB_AREA", "MIN_BLOB_AREA", "MIN_CIRCULARITY", "", "centroid", "Landroid/graphics/PointF;", "pixels", "", "Lkotlin/Pair;", "circularity", "detectAnchors", "bitmap", "Landroid/graphics/Bitmap;", "floodFill", "", "visited", "", "startX", "startY", "width", "height", "isOrangePixel", "", "pixel", "scaleBitmap", "minDim", "Blob", "app_debug"})
public final class BlobDetector {
    private static final int ANCHOR_R = 255;
    private static final int ANCHOR_G = 107;
    private static final int ANCHOR_B = 53;
    private static final int COLOR_THRESHOLD = 60;
    private static final int MIN_BLOB_AREA = 20;
    private static final int MAX_BLOB_AREA = 50000;
    private static final float MIN_CIRCULARITY = 0.3F;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.scanner.BlobDetector INSTANCE = null;
    
    private BlobDetector() {
        super();
    }
    
    /**
     * Detect anchor blobs in [bitmap].
     * @return list of up to 3 blob centroids (PointF in bitmap coordinates)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<android.graphics.PointF> detectAnchors(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> floodFill(int[] pixels, boolean[] visited, int startX, int startY, int width, int height) {
        return null;
    }
    
    private final android.graphics.PointF centroid(java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> pixels) {
        return null;
    }
    
    private final float circularity(java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> pixels) {
        return 0.0F;
    }
    
    private final boolean isOrangePixel(int pixel) {
        return false;
    }
    
    private final android.graphics.Bitmap scaleBitmap(android.graphics.Bitmap bitmap, int minDim) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0019"}, d2 = {"Lcom/hexglyph/scanner/BlobDetector$Blob;", "", "centroid", "Landroid/graphics/PointF;", "area", "", "circularity", "", "(Landroid/graphics/PointF;IF)V", "getArea", "()I", "getCentroid", "()Landroid/graphics/PointF;", "getCircularity", "()F", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class Blob {
        @org.jetbrains.annotations.NotNull()
        private final android.graphics.PointF centroid = null;
        private final int area = 0;
        private final float circularity = 0.0F;
        
        public Blob(@org.jetbrains.annotations.NotNull()
        android.graphics.PointF centroid, int area, float circularity) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.PointF getCentroid() {
            return null;
        }
        
        public final int getArea() {
            return 0;
        }
        
        public final float getCircularity() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.PointF component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final float component3() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.hexglyph.scanner.BlobDetector.Blob copy(@org.jetbrains.annotations.NotNull()
        android.graphics.PointF centroid, int area, float circularity) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}