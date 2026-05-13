package com.hexglyph.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.io.File;

/**
 * Heuristic root detection. Warning-only — cannot prevent a determined attacker
 * with physical access to a rooted device.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\u0010\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/hexglyph/utils/RootDetector;", "", "()V", "ROOT_BINARIES", "", "", "ROOT_PACKAGES", "checkBuildTags", "", "checkRootBinaries", "checkRootPackages", "context", "Landroid/content/Context;", "isRooted", "app_debug"})
public final class RootDetector {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> ROOT_BINARIES = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> ROOT_PACKAGES = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.utils.RootDetector INSTANCE = null;
    
    private RootDetector() {
        super();
    }
    
    public final boolean isRooted(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    private final boolean checkRootBinaries() {
        return false;
    }
    
    private final boolean checkBuildTags() {
        return false;
    }
    
    private final boolean checkRootPackages(android.content.Context context) {
        return false;
    }
}