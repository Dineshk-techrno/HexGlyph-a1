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
public final class StealthEncoder_Factory implements Factory<StealthEncoder> {
  private final Provider<CryptoEngine> cryptoEngineProvider;

  private final Provider<CarrierSelector> carrierSelectorProvider;

  public StealthEncoder_Factory(Provider<CryptoEngine> cryptoEngineProvider,
      Provider<CarrierSelector> carrierSelectorProvider) {
    this.cryptoEngineProvider = cryptoEngineProvider;
    this.carrierSelectorProvider = carrierSelectorProvider;
  }

  @Override
  public StealthEncoder get() {
    return newInstance(cryptoEngineProvider.get(), carrierSelectorProvider.get());
  }

  public static StealthEncoder_Factory create(Provider<CryptoEngine> cryptoEngineProvider,
      Provider<CarrierSelector> carrierSelectorProvider) {
    return new StealthEncoder_Factory(cryptoEngineProvider, carrierSelectorProvider);
  }

  public static StealthEncoder newInstance(CryptoEngine cryptoEngine,
      CarrierSelector carrierSelector) {
    return new StealthEncoder(cryptoEngine, carrierSelector);
  }
}
