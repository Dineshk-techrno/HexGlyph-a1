package com.hexglyph.renderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;

/**
 * GlyphExporter — saves rendered glyph bitmaps to disk and exposes them
 * via FileProvider for sharing with other apps.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nJ\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0004\u00a8\u0006\u0013"}, d2 = {"Lcom/hexglyph/renderer/GlyphExporter;", "", "()V", "exportPng", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "bitmap", "Landroid/graphics/Bitmap;", "filename", "", "pruneCache", "", "maxAgeMs", "", "shareIntent", "Landroid/content/Intent;", "uri", "Companion", "app_debug"})
public final class GlyphExporter {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GLYPH_DIR = "glyphs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String FILE_PREFIX = "hexglyph_";
    private static final int PNG_QUALITY = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.renderer.GlyphExporter.Companion Companion = null;
    
    @javax.inject.Inject()
    public GlyphExporter() {
        super();
    }
    
    /**
     * Save [bitmap] as a PNG in the app's private files directory.
     * @return Uri for the saved file (FileProvider content URI)
     */
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri exportPng(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.Nullable()
    java.lang.String filename) {
        return null;
    }
    
    /**
     * Build a share Intent for a glyph image URI.
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent shareIntent(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    /**
     * Delete all cached glyph images older than [maxAgeMs] milliseconds.
     */
    public final void pruneCache(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long maxAgeMs) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/hexglyph/renderer/GlyphExporter$Companion;", "", "()V", "FILE_PREFIX", "", "GLYPH_DIR", "PNG_QUALITY", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}