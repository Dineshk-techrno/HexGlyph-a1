package com.hexglyph.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Glyph canvas background colour matching spec: #0F0F1A */
private val GlyphBackground = Color(0xFF0F0F1A)

/**
 * GlyphCanvas — renders a glyph [Bitmap] (or loading/empty state) in a
 * styled square container.
 */
@Composable
fun GlyphCanvas(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?    = null,
    isLoading: Boolean = false,
    emptyLabel: String = "Your glyph will appear here"
) {
    Box(
        modifier       = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(GlyphBackground),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator(color = Color(0xFFFF6B35))
            bitmap != null -> Image(
                bitmap           = bitmap.asImageBitmap(),
                contentDescription = "HexGlyph encoded message",
                contentScale     = ContentScale.Fit,
                modifier         = Modifier.fillMaxSize()
            )
            else -> Text(
                text      = emptyLabel,
                color     = Color(0xFFF0F0F0).copy(alpha = 0.4f),
                fontSize  = 14.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(24.dp)
            )
        }
    }
}
