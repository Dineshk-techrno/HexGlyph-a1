package com.hexglyph.di;

import com.hexglyph.database.GlyphDao;
import com.hexglyph.database.GlyphDatabase;
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
public final class AppModule_ProvideGlyphDaoFactory implements Factory<GlyphDao> {
  private final Provider<GlyphDatabase> dbProvider;

  public AppModule_ProvideGlyphDaoFactory(Provider<GlyphDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GlyphDao get() {
    return provideGlyphDao(dbProvider.get());
  }

  public static AppModule_ProvideGlyphDaoFactory create(Provider<GlyphDatabase> dbProvider) {
    return new AppModule_ProvideGlyphDaoFactory(dbProvider);
  }

  public static GlyphDao provideGlyphDao(GlyphDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGlyphDao(db));
  }
}
