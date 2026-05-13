package com.hexglyph.di;

import com.hexglyph.renderer.GlyphRenderer;
import com.hexglyph.renderer.HexMath;
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
public final class AppModule_ProvideGlyphRendererFactory implements Factory<GlyphRenderer> {
  private final Provider<HexMath> hexMathProvider;

  public AppModule_ProvideGlyphRendererFactory(Provider<HexMath> hexMathProvider) {
    this.hexMathProvider = hexMathProvider;
  }

  @Override
  public GlyphRenderer get() {
    return provideGlyphRenderer(hexMathProvider.get());
  }

  public static AppModule_ProvideGlyphRendererFactory create(Provider<HexMath> hexMathProvider) {
    return new AppModule_ProvideGlyphRendererFactory(hexMathProvider);
  }

  public static GlyphRenderer provideGlyphRenderer(HexMath hexMath) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGlyphRenderer(hexMath));
  }
}
