package com.hexglyph.di;

import com.hexglyph.scanner.ScanEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideScanEngineFactory implements Factory<ScanEngine> {
  @Override
  public ScanEngine get() {
    return provideScanEngine();
  }

  public static AppModule_ProvideScanEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ScanEngine provideScanEngine() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScanEngine());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideScanEngineFactory INSTANCE = new AppModule_ProvideScanEngineFactory();
  }
}
