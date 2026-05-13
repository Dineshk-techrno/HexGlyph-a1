package com.hexglyph.di;

import android.content.Context;
import androidx.room.Room;
import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDatabase;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.renderer.GlyphExporter;
import com.hexglyph.renderer.GlyphRenderer;
import com.hexglyph.renderer.HexMath;
import com.hexglyph.scanner.ScanEngine;
import com.hexglyph.steganography.CarrierSelector;
import com.hexglyph.steganography.StealthDecoder;
import com.hexglyph.steganography.StealthEncoder;
import com.hexglyph.steganography.StealthScanEngine;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\r\u001a\u00020\f2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007J\b\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\b\u0010\u0016\u001a\u00020\u0015H\u0007J\u0012\u0010\u0017\u001a\u00020\b2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007J\b\u0010\u0018\u001a\u00020\u0019H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0006H\u0007J\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0004H\u0007J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001bH\u0007\u00a8\u0006#"}, d2 = {"Lcom/hexglyph/di/AppModule;", "", "()V", "provideCarrierSelector", "Lcom/hexglyph/steganography/CarrierSelector;", "provideCryptoEngine", "Lcom/hexglyph/crypto/CryptoEngine;", "keystoreManager", "Lcom/hexglyph/crypto/KeystoreManager;", "provideGlyphDao", "Lcom/hexglyph/database/GlyphDao;", "db", "Lcom/hexglyph/database/GlyphDatabase;", "provideGlyphDatabase", "context", "Landroid/content/Context;", "provideGlyphExporter", "Lcom/hexglyph/renderer/GlyphExporter;", "provideGlyphRenderer", "Lcom/hexglyph/renderer/GlyphRenderer;", "hexMath", "Lcom/hexglyph/renderer/HexMath;", "provideHexMath", "provideKeystoreManager", "provideScanEngine", "Lcom/hexglyph/scanner/ScanEngine;", "provideStealthDecoder", "Lcom/hexglyph/steganography/StealthDecoder;", "cryptoEngine", "provideStealthEncoder", "Lcom/hexglyph/steganography/StealthEncoder;", "carrierSelector", "provideStealthScanEngine", "Lcom/hexglyph/steganography/StealthScanEngine;", "stealthDecoder", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.database.GlyphDatabase provideGlyphDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.database.GlyphDao provideGlyphDao(@org.jetbrains.annotations.NotNull()
    com.hexglyph.database.GlyphDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.crypto.KeystoreManager provideKeystoreManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.crypto.CryptoEngine provideCryptoEngine(@org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.KeystoreManager keystoreManager) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.renderer.HexMath provideHexMath() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.renderer.GlyphRenderer provideGlyphRenderer(@org.jetbrains.annotations.NotNull()
    com.hexglyph.renderer.HexMath hexMath) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.renderer.GlyphExporter provideGlyphExporter() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.scanner.ScanEngine provideScanEngine() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.steganography.CarrierSelector provideCarrierSelector() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.steganography.StealthEncoder provideStealthEncoder(@org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.CryptoEngine cryptoEngine, @org.jetbrains.annotations.NotNull()
    com.hexglyph.steganography.CarrierSelector carrierSelector) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.steganography.StealthDecoder provideStealthDecoder(@org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.CryptoEngine cryptoEngine) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.hexglyph.steganography.StealthScanEngine provideStealthScanEngine(@org.jetbrains.annotations.NotNull()
    com.hexglyph.steganography.StealthDecoder stealthDecoder) {
        return null;
    }
}