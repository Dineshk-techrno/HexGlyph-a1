package com.hexglyph.viewmodel;

import android.content.Context;
import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeystoreManager;
import com.hexglyph.database.GlyphDao;
import com.hexglyph.scanner.ScanEngine;
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
public final class DecodeViewModel_Factory implements Factory<DecodeViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<CryptoEngine> cryptoEngineProvider;

  private final Provider<KeystoreManager> keystoreManagerProvider;

  private final Provider<ScanEngine> scanEngineProvider;

  private final Provider<GlyphDao> glyphDaoProvider;

  public DecodeViewModel_Factory(Provider<Context> contextProvider,
      Provider<CryptoEngine> cryptoEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider, Provider<ScanEngine> scanEngineProvider,
      Provider<GlyphDao> glyphDaoProvider) {
    this.contextProvider = contextProvider;
    this.cryptoEngineProvider = cryptoEngineProvider;
    this.keystoreManagerProvider = keystoreManagerProvider;
    this.scanEngineProvider = scanEngineProvider;
    this.glyphDaoProvider = glyphDaoProvider;
  }

  @Override
  public DecodeViewModel get() {
    return newInstance(contextProvider.get(), cryptoEngineProvider.get(), keystoreManagerProvider.get(), scanEngineProvider.get(), glyphDaoProvider.get());
  }

  public static DecodeViewModel_Factory create(Provider<Context> contextProvider,
      Provider<CryptoEngine> cryptoEngineProvider,
      Provider<KeystoreManager> keystoreManagerProvider, Provider<ScanEngine> scanEngineProvider,
      Provider<GlyphDao> glyphDaoProvider) {
    return new DecodeViewModel_Factory(contextProvider, cryptoEngineProvider, keystoreManagerProvider, scanEngineProvider, glyphDaoProvider);
  }

  public static DecodeViewModel newInstance(Context context, CryptoEngine cryptoEngine,
      KeystoreManager keystoreManager, ScanEngine scanEngine, GlyphDao glyphDao) {
    return new DecodeViewModel(context, cryptoEngine, keystoreManager, scanEngine, glyphDao);
  }
}
