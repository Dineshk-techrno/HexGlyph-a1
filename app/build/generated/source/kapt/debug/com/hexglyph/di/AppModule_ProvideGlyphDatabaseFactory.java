package com.hexglyph.di;

import android.content.Context;
import com.hexglyph.database.GlyphDatabase;
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
public final class AppModule_ProvideGlyphDatabaseFactory implements Factory<GlyphDatabase> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideGlyphDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GlyphDatabase get() {
    return provideGlyphDatabase(contextProvider.get());
  }

  public static AppModule_ProvideGlyphDatabaseFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideGlyphDatabaseFactory(contextProvider);
  }

  public static GlyphDatabase provideGlyphDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGlyphDatabase(context));
  }
}
