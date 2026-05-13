package com.hexglyph.di;

import com.hexglyph.renderer.GlyphExporter;
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
public final class AppModule_ProvideGlyphExporterFactory implements Factory<GlyphExporter> {
  @Override
  public GlyphExporter get() {
    return provideGlyphExporter();
  }

  public static AppModule_ProvideGlyphExporterFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlyphExporter provideGlyphExporter() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGlyphExporter());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideGlyphExporterFactory INSTANCE = new AppModule_ProvideGlyphExporterFactory();
  }
}
