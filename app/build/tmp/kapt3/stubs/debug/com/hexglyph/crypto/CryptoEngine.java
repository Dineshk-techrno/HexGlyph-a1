package com.hexglyph.crypto;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0016\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011J\u0015\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0015\u001a\u00020\bH\u0082\u0002J\f\u0010\u0016\u001a\u00020\b*\u00020\u0017H\u0002J\f\u0010\u0018\u001a\u00020\b*\u00020\u0019H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/hexglyph/crypto/CryptoEngine;", "", "keystoreManager", "Lcom/hexglyph/crypto/KeystoreManager;", "(Lcom/hexglyph/crypto/KeystoreManager;)V", "secureRandom", "Ljava/security/SecureRandom;", "aesCtr", "", "data", "key", "nonce", "encrypt", "", "decodeFull", "glyphBytes", "groupCode", "", "encodeFull", "plaintext", "plus", "other", "toBytesBigEndian", "", "toShortBigEndian", "", "Companion", "app_debug"})
public final class CryptoEngine {
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.crypto.KeystoreManager keystoreManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final byte[] MAGIC = {(byte)72, (byte)71, (byte)76, (byte)89};
    private static final byte VERSION = (byte)1;
    private static final int MAX_PLAINTEXT = 197;
    private static final int DATA_CAPACITY = 305;
    @org.jetbrains.annotations.NotNull()
    private final java.security.SecureRandom secureRandom = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.crypto.CryptoEngine.Companion Companion = null;
    
    @javax.inject.Inject()
    public CryptoEngine(@org.jetbrains.annotations.NotNull()
    com.hexglyph.crypto.KeystoreManager keystoreManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] encodeFull(@org.jetbrains.annotations.NotNull()
    byte[] plaintext, @org.jetbrains.annotations.NotNull()
    java.lang.String groupCode) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] decodeFull(@org.jetbrains.annotations.NotNull()
    byte[] glyphBytes, @org.jetbrains.annotations.NotNull()
    java.lang.String groupCode) {
        return null;
    }
    
    private final byte[] aesCtr(byte[] data, byte[] key, byte[] nonce, boolean encrypt) {
        return null;
    }
    
    private final byte[] toBytesBigEndian(long $this$toBytesBigEndian) {
        return null;
    }
    
    private final byte[] toShortBigEndian(int $this$toShortBigEndian) {
        return null;
    }
    
    private final byte[] plus(byte[] $this$plus, byte[] other) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/hexglyph/crypto/CryptoEngine$Companion;", "", "()V", "DATA_CAPACITY", "", "MAGIC", "", "MAX_PLAINTEXT", "VERSION", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}