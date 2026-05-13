package com.hexglyph.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeyDerivation;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.database.GlyphEntity;
import com.hexglyph.renderer.GlyphExporter;
import com.hexglyph.renderer.GlyphRenderer;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001*B9\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020\u0011J\u000e\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020\u0011J\u0006\u0010&\u001a\u00020!J\u0016\u0010\'\u001a\u00020!2\u0006\u0010(\u001a\u00020)2\u0006\u0010\u0002\u001a\u00020\u0003R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00140\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0019\u00a8\u0006+"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "cryptoEngine", "Lcom/hexglyph/crypto/CryptoEngine;", "keystoreManager", "Lcom/hexglyph/crypto/KeystoreManager;", "glyphRenderer", "Lcom/hexglyph/renderer/GlyphRenderer;", "glyphExporter", "Lcom/hexglyph/renderer/GlyphExporter;", "glyphDao", "Lcom/hexglyph/database/GlyphDao;", "(Landroid/content/Context;Lcom/hexglyph/crypto/CryptoEngine;Lcom/hexglyph/crypto/KeystoreManager;Lcom/hexglyph/renderer/GlyphRenderer;Lcom/hexglyph/renderer/GlyphExporter;Lcom/hexglyph/database/GlyphDao;)V", "_groupCode", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_plaintext", "_state", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "charCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "getCharCount", "()Lkotlinx/coroutines/flow/StateFlow;", "groupCode", "getGroupCode", "plaintext", "getPlaintext", "state", "getState", "encode", "", "onGroupCodeChange", "code", "onPlaintextChange", "text", "reset", "shareGlyph", "uri", "Landroid/net/Uri;", "EncodeState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EncodeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.CryptoEngine cryptoEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.KeystoreManager keystoreManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.renderer.GlyphRenderer glyphRenderer = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.renderer.GlyphExporter glyphExporter = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.database.GlyphDao glyphDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.hexglyph.viewmodel.EncodeViewModel.EncodeState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.EncodeViewModel.EncodeState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _plaintext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> plaintext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _groupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> groupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> charCount = null;
    
    @javax.inject.Inject()
    public EncodeViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.CryptoEngine cryptoEngine, @org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.KeystoreManager keystoreManager, @org.jetbrains.annotations.NotNull()
    com.hexglyph.renderer.GlyphRenderer glyphRenderer, @org.jetbrains.annotations.NotNull()
    com.hexglyph.renderer.GlyphExporter glyphExporter, @org.jetbrains.annotations.NotNull()
    com.hexglyph.database.GlyphDao glyphDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.EncodeViewModel.EncodeState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getPlaintext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getGroupCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCharCount() {
        return null;
    }
    
    public final void onPlaintextChange(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void onGroupCodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void encode() {
    }
    
    public final void reset() {
    }
    
    public final void shareGlyph(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "", "()V", "Error", "Idle", "Loading", "Success", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Error;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Idle;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Loading;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Success;", "app_debug"})
    public static abstract class EncodeState {
        
        private EncodeState() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Error;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Error extends com.hexglyph.viewmodel.EncodeViewModel.EncodeState {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            
            public Error(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.hexglyph.viewmodel.EncodeViewModel.EncodeState.Error copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Idle;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "()V", "app_debug"})
        public static final class Idle extends com.hexglyph.viewmodel.EncodeViewModel.EncodeState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.EncodeViewModel.EncodeState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Loading;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "()V", "app_debug"})
        public static final class Loading extends com.hexglyph.viewmodel.EncodeViewModel.EncodeState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.EncodeViewModel.EncodeState.Loading INSTANCE = null;
            
            private Loading() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0016"}, d2 = {"Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState$Success;", "Lcom/hexglyph/viewmodel/EncodeViewModel$EncodeState;", "bitmap", "Landroid/graphics/Bitmap;", "shareUri", "Landroid/net/Uri;", "(Landroid/graphics/Bitmap;Landroid/net/Uri;)V", "getBitmap", "()Landroid/graphics/Bitmap;", "getShareUri", "()Landroid/net/Uri;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class Success extends com.hexglyph.viewmodel.EncodeViewModel.EncodeState {
            @org.jetbrains.annotations.NotNull()
            private final android.graphics.Bitmap bitmap = null;
            @org.jetbrains.annotations.NotNull()
            private final android.net.Uri shareUri = null;
            
            public Success(@org.jetbrains.annotations.NotNull()
            android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
            android.net.Uri shareUri) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.graphics.Bitmap getBitmap() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.net.Uri getShareUri() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.graphics.Bitmap component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.net.Uri component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.hexglyph.viewmodel.EncodeViewModel.EncodeState.Success copy(@org.jetbrains.annotations.NotNull()
            android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
            android.net.Uri shareUri) {
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
}