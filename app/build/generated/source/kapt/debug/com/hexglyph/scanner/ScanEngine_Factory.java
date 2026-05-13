package com.hexglyph.scanner;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ScanEngine_Factory implements Factory<ScanEngine> {
  @Override
  public ScanEngine get() {
    return newInstance();
  }

  public static ScanEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ScanEngine newInstance() {
    return new ScanEngine();
  }

  private static final class InstanceHolder {
    private static final ScanEngine_Factory INSTANCE = new ScanEngine_Factory();
  }
}
