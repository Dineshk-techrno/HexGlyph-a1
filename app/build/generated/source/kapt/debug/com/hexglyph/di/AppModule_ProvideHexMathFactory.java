package com.hexglyph.di;

import com.hexglyph.renderer.HexMath;
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
public final class AppModule_ProvideHexMathFactory implements Factory<HexMath> {
  @Override
  public HexMath get() {
    return provideHexMath();
  }

  public static AppModule_ProvideHexMathFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HexMath provideHexMath() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideHexMath());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideHexMathFactory INSTANCE = new AppModule_ProvideHexMathFactory();
  }
}
