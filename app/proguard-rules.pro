# HexGlyph ProGuard rules

# Keep crypto classes — JCA reflection requires class names
-keep class com.hexglyph.crypto.** { *; }

# Keep Room entities and DAOs
-keep class com.hexglyph.database.** { *; }

# Keep Hilt-generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Keep biometric
-keep class androidx.biometric.** { *; }

# Keep security-crypto
-keep class androidx.security.crypto.** { *; }

# General Android
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
