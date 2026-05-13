package com.hexglyph.scanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.hexglyph.renderer.GlyphRenderer
import com.hexglyph.renderer.HexMath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * ScanEngine — orchestrates the full scanning pipeline.
 *
 * Supports two input modes:
 *   1. CameraX live stream  → [startCamera] / [stopCamera]
 *   2. File/URI            → [scanFromUri]
 *
 * Pipeline:
 *   Bitmap → BlobDetector (find anchors) → GridSampler (extract bits)
 *            → GlyphRenderer.bitsToBytes → CryptoEngine.decodeFull
 */
class ScanEngine @Inject constructor() {

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraExecutor: ExecutorService        = Executors.newSingleThreadExecutor()

    val frameChannel = Channel<ByteArray>(capacity = Channel.CONFLATED)

    private val hexMath = HexMath()

    // ── CameraX live scanning ────────────────────────────────────────────────

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewSurfaceProvider: Preview.SurfaceProvider,
        onGlyphDetected: (ByteArray) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewSurfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        val bytes = extractGlyphBits(imageProxy)
                        if (bytes != null) onGlyphDetected(bytes)
                        imageProxy.close()
                    }
                }

            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
    }

    // ── File-based scan ──────────────────────────────────────────────────────

    /**
     * Scan a glyph from a file URI. Returns raw data bytes or null on failure.
     */
    suspend fun scanFromUri(context: Context, uri: Uri): ByteArray? = withContext(Dispatchers.IO) {
        val bitmap = context.contentResolver.openInputStream(uri)
            ?.use { BitmapFactory.decodeStream(it) }
            ?: return@withContext null
        extractGlyphBitsFromBitmap(bitmap)
    }

    // ── Core extraction ──────────────────────────────────────────────────────

    private fun extractGlyphBits(imageProxy: ImageProxy): ByteArray? {
        val bitmap = imageProxy.toBitmap() ?: return null
        return extractGlyphBitsFromBitmap(bitmap)
    }

    fun extractGlyphBitsFromBitmap(bitmap: Bitmap): ByteArray? {
        return try {
            val anchors = BlobDetector.detectAnchors(bitmap)
            if (anchors.size < HexMath.ANCHOR_COUNT) return null

            val bits  = GridSampler.sample(bitmap, anchors, hexMath)
            val renderer = GlyphRenderer(hexMath)
            renderer.bitsToBytes(bits)
        } catch (e: Exception) {
            null
        }
    }

    // ── ImageProxy → Bitmap ──────────────────────────────────────────────────

    private fun ImageProxy.toBitmap(): Bitmap? {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes  = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
