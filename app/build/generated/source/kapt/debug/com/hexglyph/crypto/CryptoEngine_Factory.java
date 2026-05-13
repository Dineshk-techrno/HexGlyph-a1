package com.hexglyph.crypto;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CryptoEngine_Factory implements Factory<CryptoEngine> {
  private final Provider<KeystoreManager> keystoreManagerProvider;

  public CryptoEngine_Factory(Provider<KeystoreManager> keystoreManagerProvider) {
    this.keystoreManagerProvider = keystoreManagerProvider;
  }

  @Override
  public CryptoEngine get() {
    return newInstance(keystoreManagerProvider.get());
  }

  public static CryptoEngine_Factory create(Provider<KeystoreManager> keystoreManagerProvider) {
    return new CryptoEngine_Factory(keystoreManagerProvider);
  }

  public static CryptoEngine newInstance(KeystoreManager keystoreManager) {
    return new CryptoEngine(keystoreManager);
  }
}
