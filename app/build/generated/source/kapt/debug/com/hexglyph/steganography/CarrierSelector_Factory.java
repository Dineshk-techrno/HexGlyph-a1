package com.hexglyph.steganography;

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
public final class CarrierSelector_Factory implements Factory<CarrierSelector> {
  @Override
  public CarrierSelector get() {
    return newInstance();
  }

  public static CarrierSelector_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CarrierSelector newInstance() {
    return new CarrierSelector();
  }

  private static final class InstanceHolder {
    private static final CarrierSelector_Factory INSTANCE = new CarrierSelector_Factory();
  }
}
