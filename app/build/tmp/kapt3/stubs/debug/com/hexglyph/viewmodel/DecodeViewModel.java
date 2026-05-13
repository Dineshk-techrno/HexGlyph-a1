package com.hexglyph.viewmodel;

import android.content.Context;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeyDerivation;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.database.GlyphEntity;
import com.hexglyph.scanner.ScanEngine;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001:\u0001&B1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u000fJ\u0006\u0010!\u001a\u00020\u0019J\u001e\u0010\"\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u000fH\u0082@\u00a2\u0006\u0002\u0010$J\u0006\u0010%\u001a\u00020\u0019R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015\u00a8\u0006\'"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "cryptoEngine", "Lcom/hexglyph/crypto/CryptoEngine;", "keystoreManager", "Lcom/hexglyph/crypto/KeystoreManager;", "scanEngine", "Lcom/hexglyph/scanner/ScanEngine;", "glyphDao", "Lcom/hexglyph/database/GlyphDao;", "(Landroid/content/Context;Lcom/hexglyph/crypto/CryptoEngine;Lcom/hexglyph/crypto/KeystoreManager;Lcom/hexglyph/scanner/ScanEngine;Lcom/hexglyph/database/GlyphDao;)V", "_groupCode", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_state", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "groupCode", "Lkotlinx/coroutines/flow/StateFlow;", "getGroupCode", "()Lkotlinx/coroutines/flow/StateFlow;", "state", "getState", "decodeFromUri", "", "uri", "Landroid/net/Uri;", "onGlyphBytesDetected", "glyphBytes", "", "onGroupCodeChange", "code", "reset", "saveToHistory", "plaintext", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startScanning", "DecodeState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DecodeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.CryptoEngine cryptoEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.KeystoreManager keystoreManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.scanner.ScanEngine scanEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.database.GlyphDao glyphDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.hexglyph.viewmodel.DecodeViewModel.DecodeState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.DecodeViewModel.DecodeState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _groupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> groupCode = null;
    
    @javax.inject.Inject()
    public DecodeViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.CryptoEngine cryptoEngine, @org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.KeystoreManager keystoreManager, @org.jetbrains.annotations.NotNull()
    com.hexglyph.scanner.ScanEngine scanEngine, @org.jetbrains.annotations.NotNull()
    com.hexglyph.database.GlyphDao glyphDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.DecodeViewModel.DecodeState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getGroupCode() {
        return null;
    }
    
    /**
     * Called by ScanEngine with raw glyph bytes from the camera frame.
     * Triggers decode if we're in Scanning state.
     */
    public final void onGlyphBytesDetected(@org.jetbrains.annotations.NotNull()
    byte[] glyphBytes) {
    }
    
    public final void decodeFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void startScanning() {
    }
    
    public final void reset() {
    }
    
    public final void onGroupCodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    private final java.lang.Object saveToHistory(java.lang.String plaintext, java.lang.String code, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0003\u0004\u0005\u0006\u0007B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0005\b\t\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "", "()V", "Error", "Idle", "Processing", "Scanning", "Success", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Error;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Idle;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Processing;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Scanning;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Success;", "app_debug"})
    public static abstract class DecodeState {
        
        private DecodeState() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Error;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Error extends com.hexglyph.viewmodel.DecodeViewModel.DecodeState {
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
            public final com.hexglyph.viewmodel.DecodeViewModel.DecodeState.Error copy(@org.jetbrains.annotations.NotNull()
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Idle;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "()V", "app_debug"})
        public static final class Idle extends com.hexglyph.viewmodel.DecodeViewModel.DecodeState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.DecodeViewModel.DecodeState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Processing;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "()V", "app_debug"})
        public static final class Processing extends com.hexglyph.viewmodel.DecodeViewModel.DecodeState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.DecodeViewModel.DecodeState.Processing INSTANCE = null;
            
            private Processing() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Scanning;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "()V", "app_debug"})
        public static final class Scanning extends com.hexglyph.viewmodel.DecodeViewModel.DecodeState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.DecodeViewModel.DecodeState.Scanning INSTANCE = null;
            
            private Scanning() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState$Success;", "Lcom/hexglyph/viewmodel/DecodeViewModel$DecodeState;", "plaintext", "", "(Ljava/lang/String;)V", "getPlaintext", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Success extends com.hexglyph.viewmodel.DecodeViewModel.DecodeState {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String plaintext = null;
            
            public Success(@org.jetbrains.annotations.NotNull()
            java.lang.String plaintext) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getPlaintext() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.hexglyph.viewmodel.DecodeViewModel.DecodeState.Success copy(@org.jetbrains.annotations.NotNull()
            java.lang.String plaintext) {
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