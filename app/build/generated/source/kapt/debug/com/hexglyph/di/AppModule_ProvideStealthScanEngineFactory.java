package com.hexglyph.di;

import com.hexglyph.steganography.StealthDecoder;
import com.hexglyph.steganography.StealthScanEngine;
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
public final class AppModule_ProvideStealthScanEngineFactory implements Factory<StealthScanEngine> {
  private final Provider<StealthDecoder> stealthDecoderProvider;

  public AppModule_ProvideStealthScanEngineFactory(
      Provider<StealthDecoder> stealthDecoderProvider) {
    this.stealthDecoderProvider = stealthDecoderProvider;
  }

  @Override
  public StealthScanEngine get() {
    return provideStealthScanEngine(stealthDecoderProvider.get());
  }

  public static AppModule_ProvideStealthScanEngineFactory create(
      Provider<StealthDecoder> stealthDecoderProvider) {
    return new AppModule_ProvideStealthScanEngineFactory(stealthDecoderProvider);
  }

  public static StealthScanEngine provideStealthScanEngine(StealthDecoder stealthDecoder) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStealthScanEngine(stealthDecoder));
  }
}
