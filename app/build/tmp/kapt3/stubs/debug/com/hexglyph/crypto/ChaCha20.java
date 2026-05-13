package com.hexglyph.crypto;

/**
 * Pure-Kotlin ChaCha20 stream cipher (RFC 7539).
 *
 * Usage:
 *  val keystream = ChaCha20.keystream(key32, nonce12, counter = 0, length)
 *  val encrypted = ChaCha20.xor(plaintext, key32, nonce12)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J(\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\bJ0\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u0002J(\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\bJ\u0014\u0010\u0015\u001a\u00020\b*\u00020\u00042\u0006\u0010\u0016\u001a\u00020\bH\u0002\u00a8\u0006\u0017"}, d2 = {"Lcom/hexglyph/crypto/ChaCha20;", "", "()V", "block", "", "key", "nonce", "counter", "", "keystream", "length", "quarterRound", "", "state", "", "a", "b", "c", "d", "xor", "data", "leInt", "offset", "app_debug"})
public final class ChaCha20 {
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.crypto.ChaCha20 INSTANCE = null;
    
    private ChaCha20() {
        super();
    }
    
    private final void quarterRound(int[] state, int a, int b, int c, int d) {
    }
    
    private final byte[] block(byte[] key, byte[] nonce, int counter) {
        return null;
    }
    
    /**
     * Generate [length] bytes of keystream.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] keystream(@org.jetbrains.annotations.NotNull()
    byte[] key, @org.jetbrains.annotations.NotNull()
    byte[] nonce, int counter, int length) {
        return null;
    }
    
    /**
     * XOR [data] with ChaCha20 keystream — encryption = decryption.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] xor(@org.jetbrains.annotations.NotNull()
    byte[] data, @org.jetbrains.annotations.NotNull()
    byte[] key, @org.jetbrains.annotations.NotNull()
    byte[] nonce, int counter) {
        return null;
    }
    
    private final int leInt(byte[] $this$leInt, int offset) {
        return 0;
    }
}