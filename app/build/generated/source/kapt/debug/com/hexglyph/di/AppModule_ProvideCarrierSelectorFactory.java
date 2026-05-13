package com.hexglyph.di;

import com.hexglyph.steganography.CarrierSelector;
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
public final class AppModule_ProvideCarrierSelectorFactory implements Factory<CarrierSelector> {
  @Override
  public CarrierSelector get() {
    return provideCarrierSelector();
  }

  public static AppModule_ProvideCarrierSelectorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CarrierSelector provideCarrierSelector() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCarrierSelector());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideCarrierSelectorFactory INSTANCE = new AppModule_ProvideCarrierSelectorFactory();
  }
}
