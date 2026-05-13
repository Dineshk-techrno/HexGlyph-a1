package com.hexglyph.crypto;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.inject.Inject;

/**
 * Manages hardware-backed key storage via Android Keystore.
 *
 * Responsibilities:
 *  - Store/retrieve Group Code using EncryptedSharedPreferences (AES-256-GCM)
 *  - Generate and wrap per-session AES keys in the Keystore
 *  - Provide AES-GCM-256 encrypt/decrypt backed by Keystore keys
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013J\u0016\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bJ\u000e\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\n\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001f"}, d2 = {"Lcom/hexglyph/crypto/KeystoreManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "encryptedPrefs", "Landroid/content/SharedPreferences;", "getEncryptedPrefs", "()Landroid/content/SharedPreferences;", "encryptedPrefs$delegate", "Lkotlin/Lazy;", "keyStore", "Ljava/security/KeyStore;", "getKeyStore", "()Ljava/security/KeyStore;", "keyStore$delegate", "clearGroupCode", "", "decrypt", "", "ciphertext", "iv", "encrypt", "plaintext", "getOrCreateKeystoreKey", "Ljavax/crypto/SecretKey;", "loadGroupCode", "", "saveGroupCode", "groupCode", "Companion", "app_debug"})
public final class KeystoreManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEYSTORE_PROVIDER = "AndroidKeyStore";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEYSTORE_ALIAS = "hexglyph_master_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_FILE = "hexglyph_secure_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_GROUP_CODE = "group_code";
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy keyStore$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy encryptedPrefs$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.crypto.KeystoreManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public KeystoreManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final java.security.KeyStore getKeyStore() {
        return null;
    }
    
    private final javax.crypto.SecretKey getOrCreateKeystoreKey() {
        return null;
    }
    
    /**
     * Encrypt [plaintext] with the Keystore-backed AES-GCM key.
     * Returns: IV (12 bytes) + ciphertext + authTag (16 bytes)
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] encrypt(@org.jetbrains.annotations.NotNull()
    byte[] plaintext, @org.jetbrains.annotations.NotNull()
    byte[] iv) {
        return null;
    }
    
    /**
     * Decrypt data encrypted with [encrypt]. [iv] must match what was used to encrypt.
     * Throws on auth tag mismatch.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] decrypt(@org.jetbrains.annotations.NotNull()
    byte[] ciphertext, @org.jetbrains.annotations.NotNull()
    byte[] iv) {
        return null;
    }
    
    private final android.content.SharedPreferences getEncryptedPrefs() {
        return null;
    }
    
    public final void saveGroupCode(@org.jetbrains.annotations.NotNull()
    java.lang.String groupCode) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String loadGroupCode() {
        return null;
    }
    
    public final void clearGroupCode() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/hexglyph/crypto/KeystoreManager$Companion;", "", "()V", "AES_GCM", "", "GCM_TAG_LENGTH", "", "KEYSTORE_ALIAS", "KEYSTORE_PROVIDER", "KEY_GROUP_CODE", "PREFS_FILE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}