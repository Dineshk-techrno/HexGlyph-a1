package com.hexglyph.steganography

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import java.security.SecureRandom
import kotlin.math.cos
import kotlin.math.sin

/**
 * SyntheticCarrierGenerator
 *
 * Generates procedural carrier images that:
 *  - appear visually natural (dark aesthetic, metallic, cyberpunk)
 *  - contain sufficient high-frequency texture for steganographic embedding
 *  - avoid flat colour / clean gradient regions that reveal LSB artefacts
 *
 * Used as a fallback when no pool carrier can satisfy the payload size.
 */
object SyntheticCarrierGenerator {

    private val rng = SecureRandom()

    /**
     * Generate a stealth-friendly carrier of [width]×[height] pixels.
     * The style is chosen pseudo-randomly from the available generators.
     */
    fun generate(width: Int = 800, height: Int = 800): Bitmap {
        return when (rng.nextInt(5)) {
            0 -> darkNoise(width, height)
            1 -> metallicGrain(width, height)
            2 -> cyberpunkGrid(width, height)
            3 -> nocturnalRain(width, height)
            else -> industrialTexture(width, height)
        }
    }

    // ── Style generators ──────────────────────────────────────────────────────

    /** Dense Perlin-like noise on a dark background. */
    fun darkNoise(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val n = smoothNoise(x * 0.05f, y * 0.05f)
                val v = (n * 40f + 15f + rng.nextInt(12)).toInt().coerceIn(0, 255)

                bmp.setPixel(
                    x,
                    y,
                    Color.rgb(
                        v,
                        (v + rng.nextInt(5)).coerceIn(0, 255),
                        (v + rng.nextInt(8)).coerceIn(0, 255)
                    )
                )
            }
        }

        return bmp
    }

    /** Metallic horizontal streaks with grain. */
    fun metallicGrain(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint()

        paint.shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            intArrayOf(
                Color.rgb(22, 22, 28),
                Color.rgb(48, 48, 56),
                Color.rgb(30, 30, 38),
                Color.rgb(55, 55, 65),
                Color.rgb(18, 18, 24)
            ),
            floatArrayOf(0f, 0.25f, 0.5f, 0.75f, 1f),
            Shader.TileMode.CLAMP
        )

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        paint.strokeWidth = 1f

        repeat(height / 3) {
            val y = rng.nextInt(height).toFloat()
            val alpha = rng.nextInt(60) + 10
            val gray = rng.nextInt(80) + 40

            paint.color = Color.argb(
                alpha,
                gray,
                gray,
                (gray + rng.nextInt(15)).coerceIn(0, 255)
            )

            canvas.drawLine(
                0f,
                y,
                width.toFloat(),
                y + rng.nextFloat() * 4f - 2f,
                paint
            )
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val p = bmp.getPixel(x, y)
                val g = rng.nextInt(18) - 9

                val nr = (Color.red(p) + g).coerceIn(0, 255)
                val ng = (Color.green(p) + g).coerceIn(0, 255)
                val nb = (Color.blue(p) + g).coerceIn(0, 255)

                bmp.setPixel(x, y, Color.rgb(nr, ng, nb))
            }
        }

        return bmp
    }

    /** Dark cyberpunk-style grid / circuit pattern. */
    fun cyberpunkGrid(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint()

        canvas.drawColor(Color.rgb(8, 8, 16))

        paint.strokeWidth = 1f

        val cellW = rng.nextInt(20) + 20
        val cellH = rng.nextInt(20) + 20

        var x = 0

        while (x < width) {
            val alpha = rng.nextInt(40) + 15

            paint.color = Color.argb(
                alpha,
                0,
                180 + rng.nextInt(75),
                120 + rng.nextInt(80)
            )

            canvas.drawLine(
                x.toFloat(),
                0f,
                x.toFloat(),
                height.toFloat(),
                paint
            )

            x += cellW
        }

        var y = 0

        while (y < height) {
            val alpha = rng.nextInt(40) + 15

            paint.color = Color.argb(
                alpha,
                0,
                160 + rng.nextInt(95),
                100 + rng.nextInt(100)
            )

            canvas.drawLine(
                0f,
                y.toFloat(),
                width.toFloat(),
                y.toFloat(),
                paint
            )

            y += cellH
        }

        paint.strokeWidth = 0f

        repeat(width * height / 400) {
            val nx = rng.nextInt(width)
            val ny = rng.nextInt(height)
            val alpha = rng.nextInt(80) + 20

            paint.color = Color.argb(alpha, 0, 220, 180)

            canvas.drawCircle(
                nx.toFloat(),
                ny.toFloat(),
                rng.nextFloat() * 2f + 0.5f,
                paint
            )
        }

        for (py in 0 until height) {
            for (px in 0 until width) {
                val p = bmp.getPixel(px, py)
                val g = rng.nextInt(14) - 7

                bmp.setPixel(
                    px,
                    py,
                    Color.rgb(
                        (Color.red(p) + g).coerceIn(0, 255),
                        (Color.green(p) + g).coerceIn(0, 255),
                        (Color.blue(p) + g).coerceIn(0, 255)
                    )
                )
            }
        }

        return bmp
    }

    /** Night rain — vertical streaks on a dark urban gradient. */
    fun nocturnalRain(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint()

        paint.shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            intArrayOf(
                Color.rgb(5, 8, 18),
                Color.rgb(12, 18, 32),
                Color.rgb(6, 10, 22)
            ),
            null,
            Shader.TileMode.CLAMP
        )

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.shader = null
        paint.strokeWidth = 1f

        repeat(width * 2) {
            val rx = rng.nextInt(width).toFloat()
            val ry = rng.nextInt(height).toFloat()
            val len = rng.nextInt(30) + 8
            val alpha = rng.nextInt(70) + 15
            val bright = rng.nextInt(60) + 120

            paint.color = Color.argb(
                alpha,
                bright - 20,
                bright - 10,
                bright
            )

            canvas.drawLine(
                rx,
                ry,
                rx + rng.nextFloat() * 2f - 1f,
                ry + len.toFloat(),
                paint
            )
        }

        for (py in 0 until height) {
            for (px in 0 until width) {
                val p = bmp.getPixel(px, py)
                val g = rng.nextInt(16) - 8

                bmp.setPixel(
                    px,
                    py,
                    Color.rgb(
                        (Color.red(p) + g).coerceIn(0, 255),
                        (Color.green(p) + g).coerceIn(0, 255),
                        (Color.blue(p) + g).coerceIn(0, 255)
                    )
                )
            }
        }

        return bmp
    }

    /** Industrial worn-metal surface with scratches. */
    fun industrialTexture(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint()

        canvas.drawColor(Color.rgb(28, 26, 24))

        paint.strokeWidth = 1f

        repeat(300) {
            val sx = rng.nextInt(width).toFloat()
            val sy = rng.nextInt(height).toFloat()
            val angle = rng.nextFloat() * 0.4f - 0.2f
            val len = rng.nextInt(120) + 20
            val alpha = rng.nextInt(50) + 10
            val gray = rng.nextInt(60) + 50

            paint.color = Color.argb(alpha, gray, gray, gray)

            canvas.drawLine(
                sx,
                sy,
                sx + len * cos(angle.toDouble()).toFloat(),
                sy + len * sin(angle.toDouble()).toFloat(),
                paint
            )
        }

        repeat(40) {
            val rx = rng.nextInt(width).toFloat()
            val ry = rng.nextInt(height).toFloat()
            val r = rng.nextFloat() * 30f + 5f
            val alpha = rng.nextInt(40) + 10

            paint.color = Color.argb(
                alpha,
                100 + rng.nextInt(60),
                30 + rng.nextInt(30),
                10
            )

            canvas.drawCircle(rx, ry, r, paint)
        }

        for (py in 0 until height) {
            for (px in 0 until width) {
                val p = bmp.getPixel(px, py)
                val g = rng.nextInt(22) - 11

                bmp.setPixel(
                    px,
                    py,
                    Color.rgb(
                        (Color.red(p) + g).coerceIn(0, 255),
                        (Color.green(p) + g).coerceIn(0, 255),
                        (Color.blue(p) + g).coerceIn(0, 255)
                    )
                )
            }
        }

        return bmp
    }

    // ── Value noise helper ────────────────────────────────────────────────────

    private fun smoothNoise(x: Float, y: Float): Float {
        val ix = x.toInt()
        val iy = y.toInt()

        val fx = x - ix
        val fy = y - iy

        val ux = fx * fx * (3f - 2f * fx)
        val uy = fy * fy * (3f - 2f * fy)

        val a = hash(ix, iy)
        val b = hash(ix + 1, iy)
        val c = hash(ix, iy + 1)
        val d = hash(ix + 1, iy + 1)

        return lerp(
            lerp(a, b, ux),
            lerp(c, d, ux),
            uy
        )
    }

    private fun hash(x: Int, y: Int): Float {
        var n = (x * 374761393 + y * 668265263).toLong()
        n = (n xor (n shr 13)) * 1274126177L

        return (
            ((n xor (n shr 16)) and 0x7FFFFFFFL)
                .toFloat() / 0x7FFFFFFF.toFloat()
        )
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }
}                ))
            }
        }
        return bmp
    }

    /** Night rain — vertical streaks on a dark urban gradient. */
    fun nocturnalRain(width: Int, height: Int): Bitmap {
        val bmp    = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint  = Paint()

        paint.shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(Color.rgb(5, 8, 18), Color.rgb(12, 18, 32), Color.rgb(6, 10, 22)),
            null, Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null
        paint.strokeWidth = 1f

        // Rain streaks
        repeat(width * 2) {
            val rx     = rng.nextInt(width).toFloat()
            val ry     = rng.nextInt(height).toFloat()
            val len    = rng.nextInt(30) + 8
            val alpha  = rng.nextInt(70) + 15
            val bright = rng.nextInt(60) + 120
            paint.color = Color.argb(alpha, bright - 20, bright - 10, bright)
            canvas.drawLine(rx, ry, rx + rng.nextFloat() * 2 - 1, ry + len, paint)
        }

        // Noise
        for (py in 0 until height) {
            for (px in 0 until width) {
                val p  = bmp.getPixel(px, py)
                val g  = rng.nextInt(16) - 8
                bmp.setPixel(px, py, Color.rgb(
                    (Color.red(p)   + g).coerceIn(0, 255),
                    (Color.green(p) + g).coerceIn(0, 255),
                    (Color.blue(p)  + g).coerceIn(0, 255)
                ))
            }
        }
        return bmp
    }

    /** Industrial worn-metal surface with scratches. */
    fun industrialTexture(width: Int, height: Int): Bitmap {
        val bmp    = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint  = Paint()

        canvas.drawColor(Color.rgb(28, 26, 24))

        // Diagonal scratches
        paint.strokeWidth = 1f
        repeat(300) {
            val sx    = rng.nextInt(width).toFloat()
            val sy    = rng.nextInt(height).toFloat()
            val angle = rng.nextFloat() * 0.4f - 0.2f   // near-horizontal
            val len   = rng.nextInt(120) + 20
            val alpha = rng.nextInt(50) + 10
            val gray  = rng.nextInt(60) + 50
            paint.color = Color.argb(alpha, gray, gray, gray)
            canvas.drawLine(
                sx, sy,
                sx + len * cos(angle.toDouble()).toFloat(),
                sy + len * sin(angle.toDouble()).toFloat(),
                paint
            )
        }

        // Rust patches
        repeat(40) {
            val rx    = rng.nextInt(width).toFloat()
            val ry    = rng.nextInt(height).toFloat()
            val r     = rng.nextFloat() * 30 + 5
            val alpha = rng.nextInt(40) + 10
            paint.color = Color.argb(alpha, 100 + rng.nextInt(60), 30 + rng.nextInt(30), 10)
            canvas.drawCircle(rx, ry, r, paint)
        }

        // Fine grain
        for (py in 0 until height) {
            for (px in 0 until width) {
                val p  = bmp.getPixel(px, py)
                val g  = rng.nextInt(22) - 11
                bmp.setPixel(px, py, Color.rgb(
                    (Color.red(p)   + g).coerceIn(0, 255),
                    (Color.green(p) + g).coerceIn(0, 255),
                    (Color.blue(p)  + g).coerceIn(0, 255)
                ))
            }
        }
        return bmp
    }

    // ── Value noise helper ────────────────────────────────────────────────────

    private fun smoothNoise(x: Float, y: Float): Float {
        val ix = x.toInt(); val iy = y.toInt()
        val fx = x - ix;    val fy = y - iy
        val ux = fx * fx * (3 - 2 * fx)
        val uy = fy * fy * (3 - 2 * fy)
        val a  = hash(ix, iy);     val b = hash(ix + 1, iy)
        val c  = hash(ix, iy + 1); val d = hash(ix + 1, iy + 1)
        return lerp(lerp(a, b, ux), lerp(c, d, ux), uy)
    }

    private fun hash(x: Int, y: Int): Float {
        var n = (x * 374761393 + y * 668265263).toLong()
        n = (n xor (n shr 13)) * 1274126177L
        return ((n xor (n shr 16)) and 0x7FFFFFFFL).toFloat() / 0x7FFFFFFF.toFloat()
    }

    private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t
}
