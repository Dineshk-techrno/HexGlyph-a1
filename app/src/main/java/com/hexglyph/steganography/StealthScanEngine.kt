package com.hexglyph.steganography

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import javax.inject.Inject

/**
 * StealthScanEngine
 *
 * Detection-side counterpart to [StealthEncoder]. Provides:
 *
 *  - [hasStealthPayload]  — fast check: does this image carry a hidden glyph?
 *  - [decodeFromBitmap]   — full decode from an in-memory [Bitmap].
 *  - [decodeFromUri]      — convenience wrapper: loads URI → Bitmap → decode.
 *
 * The existing [com.hexglyph.scanner.ScanEngine] is untouched; this class
 * handles only the invisible Stealth Glyph path.
 */
class StealthScanEngine @Inject constructor(
    private val stealthDecoder: StealthDecoder
) {

    /**
     * Returns true if [bitmap] appears to contain a hidden HexGlyph payload.
     * Fast path: only reads 32 bits from the canonical signature slot sequence.
     */
    fun hasStealthPayload(bitmap: Bitmap): Boolean =
        SignatureDetector.hasSignature(bitmap)

    /**
     * Attempt to extract and decrypt the hidden payload from [bitmap].
     *
     * @param bitmap    Carrier image (ARGB_8888 preferred; will be converted if needed).
     * @param groupCode Shared secret used during encoding.
     * @return Decrypted plaintext on success.
     * @throws Exception on signature mismatch, checksum failure, or decryption error.
     */
    fun decodeFromBitmap(bitmap: Bitmap, groupCode: String): String {
        val argbBitmap = ensureArgb8888(bitmap)
        return stealthDecoder.decode(argbBitmap, groupCode)
    }

    /**
     * Load image from [uri] and attempt stealth decode.
     *
     * @return Decrypted plaintext, or null if the image cannot be loaded.
     * @throws Exception if the image loads but decoding fails.
     */
    fun decodeFromUri(context: Context, uri: Uri, groupCode: String): String? {
        val bitmap = loadBitmapFromUri(context, uri) ?: return null
        return decodeFromBitmap(bitmap, groupCode)
    }

    /**
     * Check a URI-based image for a hidden payload without decrypting it.
     * Useful for a quick "is this a stealth image?" indicator in the UI.
     */
    fun hasStealthPayloadInUri(context: Context, uri: Uri): Boolean {
        val bitmap = loadBitmapFromUri(context, uri) ?: return false
        return hasStealthPayload(bitmap)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) { null }
    }

    private fun ensureArgb8888(bitmap: Bitmap): Bitmap {
        return if (bitmap.config == Bitmap.Config.ARGB_8888) bitmap
        else bitmap.copy(Bitmap.Config.ARGB_8888, false)
    }
}
