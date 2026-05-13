package com.hexglyph.di;

import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.steganography.CarrierSelector;
import com.hexglyph.steganography.StealthEncoder;
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
public final class AppModule_ProvideStealthEncoderFactory implements Factory<StealthEncoder> {
  private final Provider<CryptoEngine> cryptoEngineProvider;

  private final Provider<CarrierSelector> carrierSelectorProvider;

  public AppModule_ProvideStealthEncoderFactory(Provider<CryptoEngine> cryptoEngineProvider,
      Provider<CarrierSelector> carrierSelectorProvider) {
    this.cryptoEngineProvider = cryptoEngineProvider;
    this.carrierSelectorProvider = carrierSelectorProvider;
  }

  @Override
  public StealthEncoder get() {
    return provideStealthEncoder(cryptoEngineProvider.get(), carrierSelectorProvider.get());
  }

  public static AppModule_ProvideStealthEncoderFactory create(
      Provider<CryptoEngine> cryptoEngineProvider,
      Provider<CarrierSelector> carrierSelectorProvider) {
    return new AppModule_ProvideStealthEncoderFactory(cryptoEngineProvider, carrierSelectorProvider);
  }

  public static StealthEncoder provideStealthEncoder(CryptoEngine cryptoEngine,
      CarrierSelector carrierSelector) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStealthEncoder(cryptoEngine, carrierSelector));
  }
}
