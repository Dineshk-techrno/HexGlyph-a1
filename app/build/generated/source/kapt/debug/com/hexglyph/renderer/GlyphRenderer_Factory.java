package com.hexglyph.renderer;

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
public final class GlyphRenderer_Factory implements Factory<GlyphRenderer> {
  private final Provider<HexMath> hexMathProvider;

  public GlyphRenderer_Factory(Provider<HexMath> hexMathProvider) {
    this.hexMathProvider = hexMathProvider;
  }

  @Override
  public GlyphRenderer get() {
    return newInstance(hexMathProvider.get());
  }

  public static GlyphRenderer_Factory create(Provider<HexMath> hexMathProvider) {
    return new GlyphRenderer_Factory(hexMathProvider);
  }

  public static GlyphRenderer newInstance(HexMath hexMath) {
    return new GlyphRenderer(hexMath);
  }
}
