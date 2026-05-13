package com.hexglyph.steganography;

import android.graphics.Bitmap;
import com.hexglyph.crypto.CryptoEngine;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

/**
 * StealthDecoder
 *
 * Reverses [StealthEncoder]:
 *
 * 1. Confirms the HXGS signature is present in the carrier bitmap.
 * 2. Derives the same deterministic slot sequence used during encoding.
 * 3. Reads payload length (4-byte prefix) then the full payload bytes.
 * 4. Validates the HXGS magic, version, and CRC-32 checksum.
 * 5. Optionally strips Group Protection outer layer.
 * 6. Passes the inner encrypted block to [CryptoEngine.decodeFull].
 * 7. Returns the recovered plaintext string.
 *
 * All exceptions are propagated to the caller (ViewModel) for UI display.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u00142\u00020\u0001:\u0002\u0014\u0015B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006H\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nJ\u0018\u0010\u000e\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002J\u0018\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/hexglyph/steganography/StealthDecoder;", "", "cryptoEngine", "Lcom/hexglyph/crypto/CryptoEngine;", "(Lcom/hexglyph/crypto/CryptoEngine;)V", "concatBytes", "", "a", "b", "decode", "", "bitmap", "Landroid/graphics/Bitmap;", "groupCode", "deriveEmbeddingSeed", "parsePayload", "Lcom/hexglyph/steganography/StealthDecoder$ParsedPayload;", "payload", "stripGroupProtection", "block", "Companion", "ParsedPayload", "app_debug"})
public final class StealthDecoder {
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.CryptoEngine cryptoEngine = null;
    private static final int HEADER_SIZE = 14;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.steganography.StealthDecoder.Companion Companion = null;
    
    @javax.inject.Inject()
    public StealthDecoder(@org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.CryptoEngine cryptoEngine) {
        super();
    }
    
    /**
     * Attempt to extract and decrypt a hidden HexGlyph payload from [bitmap].
     *
     * @param bitmap     Carrier image (must be ARGB_8888).
     * @param groupCode  Shared secret — must match the one used during encoding.
     * @return Decrypted plaintext string.
     * @throws IllegalStateException / IllegalArgumentException on any failure.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String decode(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    java.lang.String groupCode) {
        return null;
    }
    
    private final com.hexglyph.steganography.StealthDecoder.ParsedPayload parsePayload(byte[] payload) {
        return null;
    }
    
    private final byte[] stripGroupProtection(byte[] block, java.lang.String groupCode) {
        return null;
    }
    
    private final byte[] deriveEmbeddingSeed(java.lang.String groupCode, android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final byte[] concatBytes(byte[] a, byte[] b) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/hexglyph/steganography/StealthDecoder$Companion;", "", "()V", "HEADER_SIZE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lcom/hexglyph/steganography/StealthDecoder$ParsedPayload;", "", "groupProtected", "", "encryptedBlock", "", "(Z[B)V", "getEncryptedBlock", "()[B", "getGroupProtected", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
    static final class ParsedPayload {
        private final boolean groupProtected = false;
        @org.jetbrains.annotations.NotNull()
        private final byte[] encryptedBlock = null;
        
        public ParsedPayload(boolean groupProtected, @org.jetbrains.annotations.NotNull()
        byte[] encryptedBlock) {
            super();
        }
        
        public final boolean getGroupProtected() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] getEncryptedBlock() {
            return null;
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.hexglyph.steganography.StealthDecoder.ParsedPayload copy(boolean groupProtected, @org.jetbrains.annotations.NotNull()
        byte[] encryptedBlock) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}