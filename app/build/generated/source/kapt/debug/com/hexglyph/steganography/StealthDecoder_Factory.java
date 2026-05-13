package com.hexglyph.steganography;

import com.hexglyph.crypto.CryptoEngine;
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
public final class StealthDecoder_Factory implements Factory<StealthDecoder> {
  private final Provider<CryptoEngine> cryptoEngineProvider;

  public StealthDecoder_Factory(Provider<CryptoEngine> cryptoEngineProvider) {
    this.cryptoEngineProvider = cryptoEngineProvider;
  }

  @Override
  public StealthDecoder get() {
    return newInstance(cryptoEngineProvider.get());
  }

  public static StealthDecoder_Factory create(Provider<CryptoEngine> cryptoEngineProvider) {
    return new StealthDecoder_Factory(cryptoEngineProvider);
  }

  public static StealthDecoder newInstance(CryptoEngine cryptoEngine) {
    return new StealthDecoder(cryptoEngine);
  }
}
