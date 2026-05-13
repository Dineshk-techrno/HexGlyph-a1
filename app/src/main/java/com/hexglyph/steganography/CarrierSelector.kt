package com.hexglyph.steganography

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.security.SecureRandom
import javax.inject.Inject

/**
 * CarrierSelector
 *
 * Selects or generates an appropriate carrier image for steganographic
 * embedding. Priority order:
 *
 *  1. User-supplied URI  — decoded to a mutable ARGB_8888 bitmap.
 *  2. Pool carrier       — one of the assets/carriers/ images bundled in the
 *                          APK, selected pseudo-randomly to avoid repetition.
 *  3. Synthetic fallback — procedurally generated via SyntheticCarrierGenerator.
 *
 * The chosen image is checked against [RegionAnalyzer] to ensure it has
 * enough high-frequency texture to accommodate the required payload.
 */
class CarrierSelector @Inject constructor() {

    private val rng = SecureRandom()

    companion object {
        /** Minimum fraction of pixels that must pass the texture threshold. */
        private const val MIN_COVERAGE = 0.15f

        /** Synthetic fallback dimensions. */
        private const val SYNTH_WIDTH  = 900
        private const val SYNTH_HEIGHT = 900

        /**
         * Names of carrier images expected under assets/carriers/.
         * Add or remove entries to match the actual bundled asset pack.
         */
        val CARRIER_ASSETS = listOf(
            "dark_001.png", "dark_002.png", "dark_003.png", "dark_004.png",
            "dark_005.png", "dark_006.png", "dark_007.png", "dark_008.png",
            "metal_001.png", "metal_002.png", "metal_003.png", "metal_004.png",
            "metal_005.png", "metal_006.png", "metal_007.png", "metal_008.png",
            "urban_001.png", "urban_002.png", "urban_003.png", "urban_004.png",
            "urban_005.png", "urban_006.png", "urban_007.png", "urban_008.png",
            "shadow_001.png", "shadow_002.png", "shadow_003.png", "shadow_004.png",
            "shadow_005.png", "shadow_006.png", "shadow_007.png", "shadow_008.png",
            "texture_001.png", "texture_002.png", "texture_003.png", "texture_004.png",
            "texture_005.png", "texture_006.png", "texture_007.png", "texture_008.png",
            "cyber_001.png", "cyber_002.png", "cyber_003.png", "cyber_004.png",
            "rain_001.png", "rain_002.png", "rain_003.png", "rain_004.png",
            "industrial_001.png", "industrial_002.png", "industrial_003.png",
            "industrial_004.png", "industrial_005.png", "industrial_006.png",
            "forest_001.png", "forest_002.png", "forest_003.png", "forest_004.png",
            "fabric_001.png", "fabric_002.png", "fabric_003.png", "fabric_004.png",
            "watch_001.png", "watch_002.png", "watch_003.png", "watch_004.png",
            "abstract_001.png", "abstract_002.png", "abstract_003.png", "abstract_004.png",
            "stone_001.png", "stone_002.png", "stone_003.png", "stone_004.png",
            "neon_001.png", "neon_002.png", "neon_003.png", "neon_004.png",
            "rust_001.png", "rust_002.png", "rust_003.png", "rust_004.png",
            "concrete_001.png", "concrete_002.png", "concrete_003.png",
            "bokeh_001.png", "bokeh_002.png", "bokeh_003.png",
            "smoke_001.png", "smoke_002.png", "smoke_003.png",
            "circuit_001.png", "circuit_002.png", "circuit_003.png",
            "grunge_001.png", "grunge_002.png", "grunge_003.png"
        )
    }

    /**
     * Select a suitable mutable carrier bitmap that can hold [requiredBytes].
     *
     * @param context          Android context (for asset/content access).
     * @param userUri          Optional URI supplied by the user.
     * @param requiredBytes    Minimum payload capacity needed.
     * @return A mutable [Bitmap] ready for LSB embedding, or null on failure.
     */
    fun selectCarrier(
        context:       Context,
        userUri:       Uri?,
        requiredBytes: Int
    ): Bitmap? {
        // 1. User-supplied image
        if (userUri != null) {
            val bmp = loadMutableBitmap(context, userUri)
            if (bmp != null && hasCapacity(bmp, requiredBytes)) return bmp
        }

        // 2. Asset pool — shuffle order using SecureRandom for rotation
        val shuffledAssets = CARRIER_ASSETS.toMutableList().also {
            for (i in it.indices.reversed()) {
                val j = rng.nextInt(i + 1)
                val tmp = it[i]; it[i] = it[j]; it[j] = tmp
            }
        }
        for (name in shuffledAssets) {
            val bmp = loadAssetCarrier(context, "carriers/$name") ?: continue
            if (hasCapacity(bmp, requiredBytes)) return bmp
        }

        // 3. Synthetic fallback
        val synth = SyntheticCarrierGenerator.generate(SYNTH_WIDTH, SYNTH_HEIGHT)
        if (hasCapacity(synth, requiredBytes)) return synth

        // Last resort: generate progressively larger synthetic images
        for (scale in listOf(1200, 1600, 2048)) {
            val large = SyntheticCarrierGenerator.generate(scale, scale)
            if (hasCapacity(large, requiredBytes)) return large
        }
        return null
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Returns true if [bitmap] has enough textured capacity for [bytes]. */
    private fun hasCapacity(bitmap: Bitmap, bytes: Int): Boolean {
        val mask       = RegionAnalyzer.buildEmbeddingMask(bitmap)
        val coverage   = RegionAnalyzer.coverageFraction(mask)
        val maxBytes   = RegionAnalyzer.maxPayloadBytes(mask)
        return coverage >= MIN_COVERAGE && maxBytes >= bytes
    }

    /** Load a bitmap from a content URI, convert to mutable ARGB_8888. */
    private fun loadMutableBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use {
                val bmp = BitmapFactory.decodeStream(it) ?: return null
                bmp.copy(Bitmap.Config.ARGB_8888, true)
            }
        } catch (e: Exception) { null }
    }

    /** Load a bundled asset carrier, convert to mutable ARGB_8888. */
    private fun loadAssetCarrier(context: Context, assetPath: String): Bitmap? {
        return try {
            context.assets.open(assetPath).use {
                val bmp = BitmapFactory.decodeStream(it) ?: return null
                bmp.copy(Bitmap.Config.ARGB_8888, true)
            }
        } catch (e: Exception) { null }
    }
}
