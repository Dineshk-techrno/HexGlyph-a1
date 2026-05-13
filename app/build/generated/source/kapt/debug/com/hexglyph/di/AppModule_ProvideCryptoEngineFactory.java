package com.hexglyph.di;

import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.crypto.KeystoreManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppModule_ProvideCryptoEngineFactory implements Factory<CryptoEngine> {
  private final Provider<KeystoreManager> keystoreManagerProvider;

  public AppModule_ProvideCryptoEngineFactory(Provider<KeystoreManager> keystoreManagerProvider) {
    this.keystoreManagerProvider = keystoreManagerProvider;
  }

  @Override
  public CryptoEngine get() {
    return provideCryptoEngine(keystoreManagerProvider.get());
  }

  public static AppModule_ProvideCryptoEngineFactory create(
      Provider<KeystoreManager> keystoreManagerProvider) {
    return new AppModule_ProvideCryptoEngineFactory(keystoreManagerProvider);
  }

  public static CryptoEngine provideCryptoEngine(KeystoreManager keystoreManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCryptoEngine(keystoreManager));
  }
}
