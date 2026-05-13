package com.hexglyph.renderer

import android.graphics.PointF
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * HexMath — axial coordinate hex grid geometry.
 *
 * Grid spec (from HexGlyph spec):
 *   GRID_RADIUS  = 28 rings
 *   Total cells  = 2,437  (1 + 3·r·(r+1) for r=0..28)
 *   Data cells   = 2,434  (3 anchor cells reserved)
 *   Data capacity = 305 bytes = ceil(2,434 bits / 8)  ← fixed (2 padding bits in last byte)
 *
 * Coordinate system: axial (q, r) with flat-top hexagons.
 */
class HexMath {

    companion object {
        const val GRID_RADIUS         = 28
        const val TOTAL_CELLS         = 2437
        const val DATA_CELLS          = 2434
        const val ANCHOR_COUNT        = 3
        const val DATA_CAPACITY_BYTES = 305  // ceil(2434 / 8) — 2 padding bits in last byte

        // Glyph colour constants
        const val COLOR_ANCHOR     = 0xFFFF6B35.toInt()  // #FF6B35 Orange
        const val COLOR_LIT        = 0xFFF0F0F0.toInt()  // #F0F0F0 Near-white  (bit=1)
        const val COLOR_DARK       = 0xFF1A1A2E.toInt()  // #1A1A2E Near-black  (bit=0)
        const val COLOR_BACKGROUND = 0xFF0F0F1A.toInt()  // #0F0F1A Canvas background

        // Flat-top hex: 6 axial directions
        val DIRECTIONS = listOf(
            Pair( 1,  0), Pair( 1, -1), Pair( 0, -1),
            Pair(-1,  0), Pair(-1,  1), Pair( 0,  1)
        )
    }

    // ── Spiral enumeration ───────────────────────────────────────────────────

    /**
     * Returns all hex (q,r) coordinates in outward spiral order from centre.
     * Index 0 = centre cell, then ring 1, ring 2, … ring GRID_RADIUS.
     */
    fun spiralCells(radius: Int = GRID_RADIUS): List<Pair<Int, Int>> {
        val cells = mutableListOf(Pair(0, 0))
        for (ring in 1..radius) {
            var q = ring; var r = -ring  // start at top-right corner
            for (d in 0..5) {
                val (dq, dr) = DIRECTIONS[(d + 4) % 6]
                repeat(ring) {
                    cells.add(Pair(q, r))
                    q += dq; r += dr
                }
            }
        }
        return cells
    }

    // ── Axial → pixel ─────────────────────────────────────────────────────────

    /**
     * Convert axial (q, r) to flat-top pixel centre relative to glyph centre.
     * [cellSize] is the distance from hex centre to edge midpoint (apothem).
     */
    fun axialToPixel(q: Int, r: Int, cellSize: Float): PointF {
        val x = cellSize * (3f / 2f * q)
        val y = cellSize * (sqrt(3f) / 2f * q + sqrt(3f) * r)
        return PointF(x, y)
    }

    /**
     * Convert pixel (px, py) to nearest axial hex coordinate.
     */
    fun pixelToAxial(px: Float, py: Float, cellSize: Float): Pair<Int, Int> {
        val q = (2f / 3f * px) / cellSize
        val r = (-1f / 3f * px + sqrt(3f) / 3f * py) / cellSize
        return axialRound(q, r)
    }

    private fun axialRound(q: Float, r: Float): Pair<Int, Int> {
        val s     = -q - r
        var rq    = q.roundToInt()
        var rr    = r.roundToInt()
        val rs    = s.roundToInt()
        val dq    = kotlin.math.abs(rq - q)
        val dr    = kotlin.math.abs(rr - r)
        val ds    = kotlin.math.abs(rs - s)
        if (dq > dr && dq > ds) rq = -rr - rs
        else if (dr > ds)       rr = -rq - rs
        return rq to rr
    }

    // ── Anchor positions ─────────────────────────────────────────────────────

    /**
     * Returns the axial coordinates of the 3 anchor cells.
     * Placed symmetrically at ring GRID_RADIUS at 0°, 120°, 240°.
     */
    fun anchorCells(): List<Pair<Int, Int>> = listOf(
        Pair(GRID_RADIUS,  0),
        Pair(-GRID_RADIUS / 2,  GRID_RADIUS),
        Pair(-GRID_RADIUS / 2, -GRID_RADIUS)
    )

    // ── Hex corner points ────────────────────────────────────────────────────

    /**
     * Returns 6 corner PointF for a flat-top hex centred at (cx, cy) with given size.
     */
    fun hexCorners(cx: Float, cy: Float, size: Float): List<PointF> =
        (0..5).map { i ->
            val angleDeg = 60f * i          // flat-top: 0° corner
            val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()
            PointF(cx + size * cos(angleRad), cy + size * sin(angleRad))
        }

    // ── Distance ──────────────────────────────────────────────────────────────

    fun axialDistance(q1: Int, r1: Int, q2: Int, r2: Int): Int {
        val dq = q1 - q2; val dr = r1 - r2; val ds = -dq - dr
        return (kotlin.math.abs(dq) + kotlin.math.abs(dr) + kotlin.math.abs(ds)) / 2
    }

    // ── Optimal cell size for a given bitmap dimension ───────────────────────

    fun optimalCellSize(bitmapSizePx: Int, padding: Float = 0.95f): Float {
        // Outer ring pixel radius ≈ GRID_RADIUS * cellSize * 2 (approx for flat-top)
        return (bitmapSizePx / 2f * padding) / (GRID_RADIUS * 2f)
    }
}
