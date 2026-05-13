package com.hexglyph.viewmodel;

import android.content.Context;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.renderer.GlyphExporter;
import com.hexglyph.steganography.StealthEncoder;
import com.hexglyph.steganography.StealthScanEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class StealthViewModel_Factory implements Factory<StealthViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<StealthEncoder> stealthEncoderProvider;

  private final Provider<StealthScanEngine> stealthScanEngineProvider;

  private final Provider<KeystoreManager> keystoreManagerProvider;

  private final Provider<GlyphExporter> glyphExporterProvider;

  private final Provider<GlyphDao> glyphDaoProvider;

  public StealthViewModel_Factory(Provider<Context> contextProvider,
      Provider<StealthEncoder> stealthEncoderProvider,
      Provider<StealthScanEngine> stealthScanEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider,
      Provider<GlyphExporter> glyphExporterProvider, Provider<GlyphDao> glyphDaoProvider) {
    this.contextProvider = contextProvider;
    this.stealthEncoderProvider = stealthEncoderProvider;
    this.stealthScanEngineProvider = stealthScanEngineProvider;
    this.keystoreManagerProvider = keystoreManagerProvider;
    this.glyphExporterProvider = glyphExporterProvider;
    this.glyphDaoProvider = glyphDaoProvider;
  }

  @Override
  public StealthViewModel get() {
    return newInstance(contextProvider.get(), stealthEncoderProvider.get(), stealthScanEngineProvider.get(), keystoreManagerProvider.get(), glyphExporterProvider.get(), glyphDaoProvider.get());
  }

  public static StealthViewModel_Factory create(Provider<Context> contextProvider,
      Provider<StealthEncoder> stealthEncoderProvider,
      Provider<StealthScanEngine> stealthScanEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider,
      Provider<GlyphExporter> glyphExporterProvider, Provider<GlyphDao> glyphDaoProvider) {
    return new StealthViewModel_Factory(contextProvider, stealthEncoderProvider, stealthScanEngineProvider, keystoreManagerProvider, glyphExporterProvider, glyphDaoProvider);
  }

  public static StealthViewModel newInstance(Context context, StealthEncoder stealthEncoder,
      StealthScanEngine stealthScanEngine, KeystoreManager keystoreManager,
      GlyphExporter glyphExporter, GlyphDao glyphDao) {
    return new StealthViewModel(context, stealthEncoder, stealthScanEngine, keystoreManager, glyphExporter, glyphDao);
  }
}
