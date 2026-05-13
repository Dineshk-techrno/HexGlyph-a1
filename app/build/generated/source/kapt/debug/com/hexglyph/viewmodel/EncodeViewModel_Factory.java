package com.hexglyph.viewmodel;

import android.content.Context;
import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.renderer.GlyphExporter;
import com.hexglyph.renderer.GlyphRenderer;
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
public final class EncodeViewModel_Factory implements Factory<EncodeViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<CryptoEngine> cryptoEngineProvider;

  private final Provider<KeystoreManager> keystoreManagerProvider;

  private final Provider<GlyphRenderer> glyphRendererProvider;

  private final Provider<GlyphExporter> glyphExporterProvider;

  private final Provider<GlyphDao> glyphDaoProvider;

  public EncodeViewModel_Factory(Provider<Context> contextProvider,
      Provider<CryptoEngine> cryptoEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider,
      Provider<GlyphRenderer> glyphRendererProvider, Provider<GlyphExporter> glyphExporterProvider,
      Provider<GlyphDao> glyphDaoProvider) {
    this.contextProvider = contextProvider;
    this.cryptoEngineProvider = cryptoEngineProvider;
    this.keystoreManagerProvider = keystoreManagerProvider;
    this.glyphRendererProvider = glyphRendererProvider;
    this.glyphExporterProvider = glyphExporterProvider;
    this.glyphDaoProvider = glyphDaoProvider;
  }

  @Override
  public EncodeViewModel get() {
    return newInstance(contextProvider.get(), cryptoEngineProvider.get(), keystoreManagerProvider.get(), glyphRendererProvider.get(), glyphExporterProvider.get(), glyphDaoProvider.get());
  }

  public static EncodeViewModel_Factory create(Provider<Context> contextProvider,
      Provider<CryptoEngine> cryptoEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider,
      Provider<GlyphRenderer> glyphRendererProvider, Provider<GlyphExporter> glyphExporterProvider,
      Provider<GlyphDao> glyphDaoProvider) {
    return new EncodeViewModel_Factory(contextProvider, cryptoEngineProvider, keystoreManagerProvider, glyphRendererProvider, glyphExporterProvider, glyphDaoProvider);
  }

  public static EncodeViewModel newInstance(Context context, CryptoEngine cryptoEngine,
      KeystoreManager keystoreManager, GlyphRenderer glyphRenderer, GlyphExporter glyphExporter,
      GlyphDao glyphDao) {
    return new EncodeViewModel(context, cryptoEngine, keystoreManager, glyphRenderer, glyphExporter, glyphDao);
  }
}
