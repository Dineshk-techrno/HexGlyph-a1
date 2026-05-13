package com.hexglyph.renderer

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * GlyphExporter — saves rendered glyph bitmaps to disk and exposes them
 * via FileProvider for sharing with other apps.
 */
class GlyphExporter @Inject constructor() {

    companion object {
        private const val GLYPH_DIR    = "glyphs"
        private const val FILE_PREFIX  = "hexglyph_"
        private const val PNG_QUALITY  = 100
    }

    /**
     * Save [bitmap] as a PNG in the app's private files directory.
     * @return Uri for the saved file (FileProvider content URI)
     */
    fun exportPng(context: Context, bitmap: Bitmap, filename: String? = null): Uri {
        val dir  = File(context.filesDir, GLYPH_DIR).also { it.mkdirs() }
        val name = filename ?: "$FILE_PREFIX${System.currentTimeMillis()}.png"
        val file = File(dir, name)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, out)
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    /**
     * Build a share Intent for a glyph image URI.
     */
    fun shareIntent(uri: Uri): Intent =
        Intent(Intent.ACTION_SEND).apply {
            type  = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    /**
     * Delete all cached glyph images older than [maxAgeMs] milliseconds.
     */
    fun pruneCache(context: Context, maxAgeMs: Long = 24 * 60 * 60 * 1000L) {
        val dir     = File(context.filesDir, GLYPH_DIR)
        val cutoff  = System.currentTimeMillis() - maxAgeMs
        dir.listFiles()
            ?.filter { it.lastModified() < cutoff }
            ?.forEach { it.delete() }
    }
}
