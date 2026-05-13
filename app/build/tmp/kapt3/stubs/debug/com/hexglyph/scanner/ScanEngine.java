package com.hexglyph.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.hexglyph.renderer.GlyphRenderer;
import com.hexglyph.renderer.HexMath;
import kotlinx.coroutines.Dispatchers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;

/**
 * ScanEngine — orchestrates the full scanning pipeline.
 *
 * Supports two input modes:
 *  1. CameraX live stream  → [startCamera] / [stopCamera]
 *  2. File/URI            → [scanFromUri]
 *
 * Pipeline:
 *  Bitmap → BlobDetector (find anchors) → GridSampler (extract bits)
 *           → GlyphRenderer.bitsToBytes → CryptoEngine.decodeFull
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0012\u001a\u00020\u0013J \u0010\u0014\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J2\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001b0!J\u0006\u0010\"\u001a\u00020\u001bJ\u000e\u0010#\u001a\u0004\u0018\u00010\u0013*\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/hexglyph/scanner/ScanEngine;", "", "()V", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "frameChannel", "Lkotlinx/coroutines/channels/Channel;", "", "getFrameChannel", "()Lkotlinx/coroutines/channels/Channel;", "hexMath", "Lcom/hexglyph/renderer/HexMath;", "extractGlyphBits", "imageProxy", "Landroidx/camera/core/ImageProxy;", "extractGlyphBitsFromBitmap", "bitmap", "Landroid/graphics/Bitmap;", "scanFromUri", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "(Landroid/content/Context;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startCamera", "", "lifecycleOwner", "Landroidx/lifecycle/LifecycleOwner;", "previewSurfaceProvider", "Landroidx/camera/core/Preview$SurfaceProvider;", "onGlyphDetected", "Lkotlin/Function1;", "stopCamera", "toBitmap", "app_debug"})
public final class ScanEngine {
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.lifecycle.ProcessCameraProvider cameraProvider;
    @org.jetbrains.annotations.NotNull()
    private java.util.concurrent.ExecutorService cameraExecutor;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<byte[]> frameChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.renderer.HexMath hexMath = null;
    
    @javax.inject.Inject()
    public ScanEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.channels.Channel<byte[]> getFrameChannel() {
        return null;
    }
    
    public final void startCamera(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LifecycleOwner lifecycleOwner, @org.jetbrains.annotations.NotNull()
    androidx.camera.core.Preview.SurfaceProvider previewSurfaceProvider, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super byte[], kotlin.Unit> onGlyphDetected) {
    }
    
    public final void stopCamera() {
    }
    
    /**
     * Scan a glyph from a file URI. Returns raw data bytes or null on failure.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object scanFromUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    private final byte[] extractGlyphBits(androidx.camera.core.ImageProxy imageProxy) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final byte[] extractGlyphBitsFromBitmap(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final android.graphics.Bitmap toBitmap(androidx.camera.core.ImageProxy $this$toBitmap) {
        return null;
    }
}