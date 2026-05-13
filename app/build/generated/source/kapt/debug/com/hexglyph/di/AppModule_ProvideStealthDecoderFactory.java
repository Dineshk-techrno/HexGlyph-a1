package com.hexglyph.di;

import com.hexglyph.crypto.CryptoEngine;
import com.hexglyph.steganography.StealthDecoder;
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
public final class AppModule_ProvideStealthDecoderFactory implements Factory<StealthDecoder> {
  private final Provider<CryptoEngine> cryptoEngineProvider;

  public AppModule_ProvideStealthDecoderFactory(Provider<CryptoEngine> cryptoEngineProvider) {
    this.cryptoEngineProvider = cryptoEngineProvider;
  }

  @Override
  public StealthDecoder get() {
    return provideStealthDecoder(cryptoEngineProvider.get());
  }

  public static AppModule_ProvideStealthDecoderFactory create(
      Provider<CryptoEngine> cryptoEngineProvider) {
    return new AppModule_ProvideStealthDecoderFactory(cryptoEngineProvider);
  }

  public static StealthDecoder provideStealthDecoder(CryptoEngine cryptoEngine) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStealthDecoder(cryptoEngine));
  }
}
