package com.hexglyph.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.hexglyph.crypto.KeyDerivation;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.database.GlyphEntity;
import com.hexglyph.renderer.GlyphExporter;
import com.hexglyph.steganography.StealthEncoder;
import com.hexglyph.steganography.StealthScanEngine;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001:\u0001:B9\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0011J\u0006\u0010.\u001a\u00020,J\u000e\u0010/\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0011J\u0010\u00100\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u0011J\u000e\u00101\u001a\u00020,2\u0006\u00102\u001a\u00020\u0013J\u000e\u00103\u001a\u00020,2\u0006\u00102\u001a\u00020\u0013J\u000e\u00104\u001a\u00020,2\u0006\u00105\u001a\u00020\u0016J\u000e\u00106\u001a\u00020,2\u0006\u00107\u001a\u00020\u0013J\u0006\u00108\u001a\u00020,J\u0016\u00109\u001a\u00020,2\u0006\u0010-\u001a\u00020\u00112\u0006\u0010\u0002\u001a\u00020\u0003R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001dR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001dR\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00160\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001dR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00190\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001dR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "stealthEncoder", "Lcom/hexglyph/steganography/StealthEncoder;", "stealthScanEngine", "Lcom/hexglyph/steganography/StealthScanEngine;", "keystoreManager", "Lcom/hexglyph/crypto/KeystoreManager;", "glyphExporter", "Lcom/hexglyph/renderer/GlyphExporter;", "glyphDao", "Lcom/hexglyph/database/GlyphDao;", "(Landroid/content/Context;Lcom/hexglyph/steganography/StealthEncoder;Lcom/hexglyph/steganography/StealthScanEngine;Lcom/hexglyph/crypto/KeystoreManager;Lcom/hexglyph/renderer/GlyphExporter;Lcom/hexglyph/database/GlyphDao;)V", "_carrierUri", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Landroid/net/Uri;", "_decodeGroupCode", "", "_groupCode", "_groupProtected", "", "_plaintext", "_state", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "carrierUri", "Lkotlinx/coroutines/flow/StateFlow;", "getCarrierUri", "()Lkotlinx/coroutines/flow/StateFlow;", "charCount", "", "getCharCount", "decodeGroupCode", "getDecodeGroupCode", "groupCode", "getGroupCode", "groupProtected", "getGroupProtected", "plaintext", "getPlaintext", "state", "getState", "detectAndDecodeFromUri", "", "uri", "encodeStealthGlyph", "inspectUri", "onCarrierUriSelected", "onDecodeGroupCodeChange", "code", "onGroupCodeChange", "onGroupProtectedChange", "enabled", "onPlaintextChange", "text", "reset", "shareStealthImage", "StealthState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StealthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.steganography.StealthEncoder stealthEncoder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.steganography.StealthScanEngine stealthScanEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.KeystoreManager keystoreManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.renderer.GlyphExporter glyphExporter = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.database.GlyphDao glyphDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.hexglyph.viewmodel.StealthViewModel.StealthState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.StealthViewModel.StealthState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _plaintext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> plaintext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _groupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> groupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _groupProtected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> groupProtected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<android.net.Uri> _carrierUri = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<android.net.Uri> carrierUri = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> charCount = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _decodeGroupCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> decodeGroupCode = null;
    
    @javax.inject.Inject()
    public StealthViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.hexglyph.steganography.StealthEncoder stealthEncoder, @org.jetbrains.annotations.NotNull()
    com.hexglyph.steganography.StealthScanEngine stealthScanEngine, @org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.KeystoreManager keystoreManager, @org.jetbrains.annotations.NotNull()
    com.hexglyph.renderer.GlyphExporter glyphExporter, @org.jetbrains.annotations.NotNull()
    com.hexglyph.database.GlyphDao glyphDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.hexglyph.viewmodel.StealthViewModel.StealthState> getState() {
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
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getGroupProtected() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<android.net.Uri> getCarrierUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCharCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getDecodeGroupCode() {
        return null;
    }
    
    public final void onPlaintextChange(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void onGroupCodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void onGroupProtectedChange(boolean enabled) {
    }
    
    public final void onCarrierUriSelected(@org.jetbrains.annotations.Nullable()
    android.net.Uri uri) {
    }
    
    public final void onDecodeGroupCodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void reset() {
    }
    
    public final void encodeStealthGlyph() {
    }
    
    public final void detectAndDecodeFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    /**
     * Quick check — does the image at [uri] contain a hidden glyph?
     */
    public final void inspectUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void shareStealthImage(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0003\u0004\u0005\u0006\u0007\bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0006\t\n\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "", "()V", "DecodeSuccess", "EncodeSuccess", "Error", "HasPayload", "Idle", "Loading", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$DecodeSuccess;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$EncodeSuccess;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Error;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$HasPayload;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Idle;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Loading;", "app_debug"})
    public static abstract class StealthState {
        
        private StealthState() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$DecodeSuccess;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "plaintext", "", "(Ljava/lang/String;)V", "getPlaintext", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class DecodeSuccess extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String plaintext = null;
            
            public DecodeSuccess(@org.jetbrains.annotations.NotNull()
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
            public final com.hexglyph.viewmodel.StealthViewModel.StealthState.DecodeSuccess copy(@org.jetbrains.annotations.NotNull()
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0016"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$EncodeSuccess;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "bitmap", "Landroid/graphics/Bitmap;", "shareUri", "Landroid/net/Uri;", "(Landroid/graphics/Bitmap;Landroid/net/Uri;)V", "getBitmap", "()Landroid/graphics/Bitmap;", "getShareUri", "()Landroid/net/Uri;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class EncodeSuccess extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
            @org.jetbrains.annotations.NotNull()
            private final android.graphics.Bitmap bitmap = null;
            @org.jetbrains.annotations.NotNull()
            private final android.net.Uri shareUri = null;
            
            public EncodeSuccess(@org.jetbrains.annotations.NotNull()
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
            public final com.hexglyph.viewmodel.StealthViewModel.StealthState.EncodeSuccess copy(@org.jetbrains.annotations.NotNull()
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Error;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Error extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
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
            public final com.hexglyph.viewmodel.StealthViewModel.StealthState.Error copy(@org.jetbrains.annotations.NotNull()
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$HasPayload;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;)V", "getUri", "()Landroid/net/Uri;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class HasPayload extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
            @org.jetbrains.annotations.NotNull()
            private final android.net.Uri uri = null;
            
            public HasPayload(@org.jetbrains.annotations.NotNull()
            android.net.Uri uri) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.net.Uri getUri() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.net.Uri component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.hexglyph.viewmodel.StealthViewModel.StealthState.HasPayload copy(@org.jetbrains.annotations.NotNull()
            android.net.Uri uri) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Idle;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "()V", "app_debug"})
        public static final class Idle extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.StealthViewModel.StealthState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/viewmodel/StealthViewModel$StealthState$Loading;", "Lcom/hexglyph/viewmodel/StealthViewModel$StealthState;", "()V", "app_debug"})
        public static final class Loading extends com.hexglyph.viewmodel.StealthViewModel.StealthState {
            @org.jetbrains.annotations.NotNull()
            public static final com.hexglyph.viewmodel.StealthViewModel.StealthState.Loading INSTANCE = null;
            
            private Loading() {
            }
        }
    }
}