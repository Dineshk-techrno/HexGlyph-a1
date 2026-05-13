package com.hexglyph.crypto;

/**
 * Reed-Solomon error correction over GF(2^8), generator polynomial 0x11D.
 *
 * Parameters (per spec):
 *  ECC symbols  : 10 per 255-byte block
 *  Error capacity: up to 5 byte errors per block
 *  Max data     : 245 bytes per block
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\f\n\u0002\u0010\u0012\n\u0002\b\u0015\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\nH\u0002J \u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\nH\u0002J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017J\u0010\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u0017H\u0002J\u000e\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0002J\u0018\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\u0004H\u0002J\b\u0010 \u001a\u00020\nH\u0002J\u0018\u0010!\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u00042\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u0004H\u0002J\u0018\u0010&\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u00042\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0018\u0010\'\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0004H\u0002J\u0018\u0010)\u001a\u00020\n2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/hexglyph/crypto/ReedSolomon;", "", "()V", "BLOCK_SIZE", "", "ECC_SYMBOLS", "FIELD_SIZE", "GEN_POLY", "MAX_DATA", "generator", "", "getGenerator", "()[I", "generator$delegate", "Lkotlin/Lazy;", "gfExp", "gfLog", "berlekampMassey", "syndromes", "correctErrors", "msg", "errPos", "decode", "", "data", "decodeBlock", "block", "encode", "encodeBlock", "findErrors", "errLoc", "msgLen", "generatorPoly", "gfDiv", "a", "b", "gfInverse", "x", "gfMul", "gfPow", "power", "polyMul", "p", "q", "app_debug"})
public final class ReedSolomon {
    private static final int GEN_POLY = 285;
    private static final int FIELD_SIZE = 256;
    private static final int ECC_SYMBOLS = 10;
    private static final int BLOCK_SIZE = 255;
    private static final int MAX_DATA = 245;
    @org.jetbrains.annotations.NotNull()
    private static final int[] gfExp = null;
    @org.jetbrains.annotations.NotNull()
    private static final int[] gfLog = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy generator$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.hexglyph.crypto.ReedSolomon INSTANCE = null;
    
    private ReedSolomon() {
        super();
    }
    
    private final int gfMul(int a, int b) {
        return 0;
    }
    
    private final int gfDiv(int a, int b) {
        return 0;
    }
    
    private final int gfPow(int x, int power) {
        return 0;
    }
    
    private final int gfInverse(int x) {
        return 0;
    }
    
    private final int[] generatorPoly() {
        return null;
    }
    
    private final int[] getGenerator() {
        return null;
    }
    
    private final int[] polyMul(int[] p, int[] q) {
        return null;
    }
    
    /**
     * Encode a full message (arbitrary length) into RS-protected blocks.
     * Returns concatenated [data + ecc] blocks.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] encode(@org.jetbrains.annotations.NotNull()
    byte[] data) {
        return null;
    }
    
    /**
     * Decode and correct errors in RS-protected data.
     * Returns original data bytes (ECC symbols stripped).
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] decode(@org.jetbrains.annotations.NotNull()
    byte[] data) {
        return null;
    }
    
    private final byte[] encodeBlock(byte[] data) {
        return null;
    }
    
    private final byte[] decodeBlock(byte[] block) {
        return null;
    }
    
    private final int[] berlekampMassey(int[] syndromes) {
        return null;
    }
    
    private final int[] findErrors(int[] errLoc, int msgLen) {
        return null;
    }
    
    private final int[] correctErrors(int[] msg, int[] syndromes, int[] errPos) {
        return null;
    }
}