package com.hexglyph.renderer;

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
public final class GlyphExporter_Factory implements Factory<GlyphExporter> {
  @Override
  public GlyphExporter get() {
    return newInstance();
  }

  public static GlyphExporter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlyphExporter newInstance() {
    return new GlyphExporter();
  }

  private static final class InstanceHolder {
    private static final GlyphExporter_Factory INSTANCE = new GlyphExporter_Factory();
  }
}
