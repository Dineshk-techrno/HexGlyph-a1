package com.hexglyph.steganography;

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
public final class StealthScanEngine_Factory implements Factory<StealthScanEngine> {
  private final Provider<StealthDecoder> stealthDecoderProvider;

  public StealthScanEngine_Factory(Provider<StealthDecoder> stealthDecoderProvider) {
    this.stealthDecoderProvider = stealthDecoderProvider;
  }

  @Override
  public StealthScanEngine get() {
    return newInstance(stealthDecoderProvider.get());
  }

  public static StealthScanEngine_Factory create(Provider<StealthDecoder> stealthDecoderProvider) {
    return new StealthScanEngine_Factory(stealthDecoderProvider);
  }

  public static StealthScanEngine newInstance(StealthDecoder stealthDecoder) {
    return new StealthScanEngine(stealthDecoder);
  }
}
