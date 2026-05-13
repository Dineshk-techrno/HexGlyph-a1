package com.hexglyph.renderer;

import android.graphics.PointF;

/**
 * HexMath — axial coordinate hex grid geometry.
 *
 * Grid spec (from HexGlyph spec):
 *  GRID_RADIUS  = 28 rings
 *  Total cells  = 2,437  (1 + 3·r·(r+1) for r=0..28)
 *  Data cells   = 2,434  (3 anchor cells reserved)
 *  Data capacity = 305 bytes = ceil(2,434 bits / 8)  ← fixed (2 padding bits in last byte)
 *
 * Coordinate system: axial (q, r) with flat-top hexagons.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00050\u0004J&\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006J$\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u001e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u000eJ$\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00110\u00042\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u000eJ\u0018\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00062\b\b\u0002\u0010\u0019\u001a\u00020\u000eJ*\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000eJ\"\u0010\u001d\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00050\u00042\b\b\u0002\u0010\u001e\u001a\u00020\u0006\u00a8\u0006 "}, d2 = {"Lcom/hexglyph/renderer/HexMath;", "", "()V", "anchorCells", "", "Lkotlin/Pair;", "", "axialDistance", "q1", "r1", "q2", "r2", "axialRound", "q", "", "r", "axialToPixel", "Landroid/graphics/PointF;", "cellSize", "hexCorners", "cx", "cy", "size", "optimalCellSize", "bitmapSizePx", "padding", "pixelToAxial", "px", "py", "spiralCells", "radius", "Companion", "app_debug"})
public final class HexMath {
    public static final int GRID_RADIUS = 28;
    public static final int TOTAL_CELLS = 2437;
    public static final int DATA_CELLS = 2434;
    public static final int ANCHOR_COUNT = 3;
    public static final int DATA_CAPACITY_BYTES = 305;
    public static final int COLOR_ANCHOR = -38091;
    public static final int COLOR_LIT = -986896;
    public static final int COLOR_DARK = -15066578;
    public static final int COLOR_BACKGROUND = -15790310;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> DIRECTIONS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.renderer.HexMath.Companion Companion = null;
    
    public HexMath() {
        super();
    }
    
    /**
     * Returns all hex (q,r) coordinates in outward spiral order from centre.
     * Index 0 = centre cell, then ring 1, ring 2, … ring GRID_RADIUS.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> spiralCells(int radius) {
        return null;
    }
    
    /**
     * Convert axial (q, r) to flat-top pixel centre relative to glyph centre.
     * [cellSize] is the distance from hex centre to edge midpoint (apothem).
     */
    @org.jetbrains.annotations.NotNull()
    public final android.graphics.PointF axialToPixel(int q, int r, float cellSize) {
        return null;
    }
    
    /**
     * Convert pixel (px, py) to nearest axial hex coordinate.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.lang.Integer, java.lang.Integer> pixelToAxial(float px, float py, float cellSize) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> axialRound(float q, float r) {
        return null;
    }
    
    /**
     * Returns the axial coordinates of the 3 anchor cells.
     * Placed symmetrically at ring GRID_RADIUS at 0°, 120°, 240°.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> anchorCells() {
        return null;
    }
    
    /**
     * Returns 6 corner PointF for a flat-top hex centred at (cx, cy) with given size.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<android.graphics.PointF> hexCorners(float cx, float cy, float size) {
        return null;
    }
    
    public final int axialDistance(int q1, int r1, int q2, int r2) {
        return 0;
    }
    
    public final float optimalCellSize(int bitmapSizePx, float padding) {
        return 0.0F;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R#\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/hexglyph/renderer/HexMath$Companion;", "", "()V", "ANCHOR_COUNT", "", "COLOR_ANCHOR", "COLOR_BACKGROUND", "COLOR_DARK", "COLOR_LIT", "DATA_CAPACITY_BYTES", "DATA_CELLS", "DIRECTIONS", "", "Lkotlin/Pair;", "getDIRECTIONS", "()Ljava/util/List;", "GRID_RADIUS", "TOTAL_CELLS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> getDIRECTIONS() {
            return null;
        }
    }
}