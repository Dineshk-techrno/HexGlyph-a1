package com.hexglyph

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HexGlyphApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
