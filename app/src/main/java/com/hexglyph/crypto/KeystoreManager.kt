package com.hexglyph.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

/**
 * Manages hardware-backed key storage via Android Keystore.
 *
 * Responsibilities:
 *   - Store/retrieve Group Code using EncryptedSharedPreferences (AES-256-GCM)
 *   - Generate and wrap per-session AES keys in the Keystore
 *   - Provide AES-GCM-256 encrypt/decrypt backed by Keystore keys
 */
class KeystoreManager @Inject constructor(private val context: Context) {

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEYSTORE_ALIAS    = "hexglyph_master_key"
        private const val AES_GCM           = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH    = 128 // bits
        private const val PREFS_FILE        = "hexglyph_secure_prefs"
        private const val KEY_GROUP_CODE    = "group_code"
    }

    // ── Android Keystore AES key ─────────────────────────────────────────────

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_PROVIDER).also { it.load(null) }
    }

    private fun getOrCreateKeystoreKey(): SecretKey {
        keyStore.getKey(KEYSTORE_ALIAS, null)?.let { return it as SecretKey }

        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        keyGen.init(
            KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false)
                .build()
        )
        return keyGen.generateKey()
    }

    // ── AES-GCM-256 Encrypt ──────────────────────────────────────────────────

    /**
     * Encrypt [plaintext] with the Keystore-backed AES-GCM key.
     * Returns: IV (12 bytes) + ciphertext + authTag (16 bytes)
     */
    fun encrypt(plaintext: ByteArray, iv: ByteArray): ByteArray {
        val key    = getOrCreateKeystoreKey()
        val cipher = Cipher.getInstance(AES_GCM)
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        return cipher.doFinal(plaintext)  // ciphertext + 16-byte tag appended by JCA
    }

    /**
     * Decrypt data encrypted with [encrypt]. [iv] must match what was used to encrypt.
     * Throws on auth tag mismatch.
     */
    fun decrypt(ciphertext: ByteArray, iv: ByteArray): ByteArray {
        val key    = getOrCreateKeystoreKey()
        val cipher = Cipher.getInstance(AES_GCM)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        return cipher.doFinal(ciphertext)
    }

    // ── Group Code storage ───────────────────────────────────────────────────

    private val encryptedPrefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveGroupCode(groupCode: String) {
        encryptedPrefs.edit().putString(KEY_GROUP_CODE, groupCode).apply()
    }

    fun loadGroupCode(): String? =
        encryptedPrefs.getString(KEY_GROUP_CODE, null)

    fun clearGroupCode() {
        encryptedPrefs.edit().remove(KEY_GROUP_CODE).apply()
    }
}
