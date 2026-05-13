package com.hexglyph.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.io.File;

/**
 * Auto-clears sensitive clipboard content after 60 seconds.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/hexglyph/utils/SecureClipboard;", "", "()V", "CLEAR_DELAY_MS", "", "handler", "Landroid/os/Handler;", "cancelPendingClear", "", "copySecure", "context", "Landroid/content/Context;", "label", "", "text", "scheduleClear", "clipboard", "Landroid/content/ClipboardManager;", "app_debug"})
public final class SecureClipboard {
    private static final long CLEAR_DELAY_MS = 60000L;
    @org.jetbrains.annotations.NotNull()
    private static final android.os.Handler handler = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.utils.SecureClipboard INSTANCE = null;
    
    private SecureClipboard() {
        super();
    }
    
    public final void copySecure(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    private final void scheduleClear(android.content.ClipboardManager clipboard) {
    }
    
    public final void cancelPendingClear() {
    }
}