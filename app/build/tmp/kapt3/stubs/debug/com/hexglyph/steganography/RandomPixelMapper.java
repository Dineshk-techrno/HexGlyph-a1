package com.hexglyph.steganography;

import java.security.MessageDigest;

/**
 * Deterministic pseudo-random pixel mapper for steganographic embedding.
 *
 * Given a seed derived from the payload key, produces a non-repeating,
 * non-sequential stream of (x, y, channel) triples covering the entire
 * bitmap capacity. Uses a simple LCG seeded from SHA-256(seed) — same
 * approach as KeyDerivation.SeededRng so behaviour is consistent across
 * the codebase.
 *
 * Channel mapping:  0 → R,  1 → G,  2 → B
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\r\u000eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/hexglyph/steganography/RandomPixelMapper;", "", "seed", "", "([B)V", "rng", "Lcom/hexglyph/steganography/RandomPixelMapper$SeededRng;", "buildSlotSequence", "", "Lcom/hexglyph/steganography/RandomPixelMapper$SlotAddress;", "width", "", "height", "SeededRng", "SlotAddress", "app_debug"})
public final class RandomPixelMapper {
    @org.jetbrains.annotations.NotNull()
    private final com.hexglyph.steganography.RandomPixelMapper.SeededRng rng = null;
    
    public RandomPixelMapper(@org.jetbrains.annotations.NotNull()
    byte[] seed) {
        super();
    }
    
    /**
     * Build a shuffled index list of every (pixelIndex * 3 + channel) slot
     * within a bitmap of [width]×[height] pixels.
     *
     * Returns a list of [SlotAddress] in randomised order. Callers iterate
     * through the list to assign one payload bit per slot.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.hexglyph.steganography.RandomPixelMapper.SlotAddress> buildSlotSequence(int width, int height) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/hexglyph/steganography/RandomPixelMapper$SeededRng;", "", "seed", "", "([B)V", "state", "", "nextInt", "", "bound", "app_debug"})
    static final class SeededRng {
        private long state;
        
        public SeededRng(@org.jetbrains.annotations.NotNull()
        byte[] seed) {
            super();
        }
        
        public final int nextInt(int bound) {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/hexglyph/steganography/RandomPixelMapper$SlotAddress;", "", "x", "", "y", "channel", "(III)V", "getChannel", "()I", "getX", "getY", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class SlotAddress {
        private final int x = 0;
        private final int y = 0;
        private final int channel = 0;
        
        public SlotAddress(int x, int y, int channel) {
            super();
        }
        
        public final int getX() {
            return 0;
        }
        
        public final int getY() {
            return 0;
        }
        
        public final int getChannel() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.hexglyph.steganography.RandomPixelMapper.SlotAddress copy(int x, int y, int channel) {
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