package com.hexglyph.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Spec colours
val Orange    = Color(0xFFFF6B35)
val NearWhite = Color(0xFFF0F0F0)
val NearBlack = Color(0xFF1A1A2E)
val Canvas    = Color(0xFF0F0F1A)

private val HexGlyphDarkColors = darkColorScheme(
    primary          = Orange,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFF7A3010),
    secondary        = NearWhite,
    onSecondary      = NearBlack,
    background       = Canvas,
    onBackground     = NearWhite,
    surface          = NearBlack,
    onSurface        = NearWhite,
    surfaceVariant   = Color(0xFF1E1E30),
    onSurfaceVariant = Color(0xFFB0B0C0),
    error            = Color(0xFFCF6679),
    outline          = Color(0xFF3A3A5A),
)

@Composable
fun HexGlyphTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HexGlyphDarkColors,
        typography  = Typography(),
        content     = content
    )
}
