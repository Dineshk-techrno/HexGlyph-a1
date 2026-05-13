package com.hexglyph.di

import android.content.Context
import androidx.room.Room
import com.hexglyph.crypto.CryptoEngine
import com.hexglyph.crypto.KeystoreManager
import com.hexglyph.database.GlyphDatabase
import com.hexglyph.database.GlyphDao
import com.hexglyph.renderer.GlyphExporter
import com.hexglyph.renderer.GlyphRenderer
import com.hexglyph.renderer.HexMath
import com.hexglyph.scanner.ScanEngine
import com.hexglyph.steganography.CarrierSelector
import com.hexglyph.steganography.StealthDecoder
import com.hexglyph.steganography.StealthEncoder
import com.hexglyph.steganography.StealthScanEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGlyphDatabase(@ApplicationContext context: Context): GlyphDatabase =
        Room.databaseBuilder(context, GlyphDatabase::class.java, "hexglyph.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideGlyphDao(db: GlyphDatabase): GlyphDao = db.glyphDao()

    @Provides
    @Singleton
    fun provideKeystoreManager(@ApplicationContext context: Context): KeystoreManager =
        KeystoreManager(context)

    @Provides
    @Singleton
    fun provideCryptoEngine(keystoreManager: KeystoreManager): CryptoEngine =
        CryptoEngine(keystoreManager)

    @Provides
    @Singleton
    fun provideHexMath(): HexMath = HexMath()

    @Provides
    @Singleton
    fun provideGlyphRenderer(hexMath: HexMath): GlyphRenderer = GlyphRenderer(hexMath)

    @Provides
    @Singleton
    fun provideGlyphExporter(): GlyphExporter = GlyphExporter()

    @Provides
    @Singleton
    fun provideScanEngine(): ScanEngine = ScanEngine()

    // ── Steganography layer ───────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideCarrierSelector(): CarrierSelector = CarrierSelector()

    @Provides
    @Singleton
    fun provideStealthEncoder(
        cryptoEngine:    CryptoEngine,
        carrierSelector: CarrierSelector
    ): StealthEncoder = StealthEncoder(cryptoEngine, carrierSelector)

    @Provides
    @Singleton
    fun provideStealthDecoder(cryptoEngine: CryptoEngine): StealthDecoder =
        StealthDecoder(cryptoEngine)

    @Provides
    @Singleton
    fun provideStealthScanEngine(stealthDecoder: StealthDecoder): StealthScanEngine =
        StealthScanEngine(stealthDecoder)
}
