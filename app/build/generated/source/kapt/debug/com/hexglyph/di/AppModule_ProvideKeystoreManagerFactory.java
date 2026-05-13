package com.hexglyph.di;

import android.content.Context;
import com.hexglyph.crypto.KeystoreManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideKeystoreManagerFactory implements Factory<KeystoreManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideKeystoreManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public KeystoreManager get() {
    return provideKeystoreManager(contextProvider.get());
  }

  public static AppModule_ProvideKeystoreManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideKeystoreManagerFactory(contextProvider);
  }

  public static KeystoreManager provideKeystoreManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideKeystoreManager(context));
  }
}
