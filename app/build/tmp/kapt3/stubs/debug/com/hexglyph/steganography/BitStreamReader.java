package com.hexglyph.steganography;

import android.graphics.Bitmap;

/**
 * BitStreamReader
 *
 * Extracts bits from the least-significant bits of the nominated pixel
 * channels in [bitmap], following the same [slots] address sequence as the
 * corresponding [BitStreamWriter].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\n\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tJ\u0006\u0010\u000e\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/hexglyph/steganography/BitStreamReader;", "", "bitmap", "Landroid/graphics/Bitmap;", "slots", "", "Lcom/hexglyph/steganography/RandomPixelMapper$SlotAddress;", "(Landroid/graphics/Bitmap;Ljava/util/List;)V", "slotIndex", "", "readBit", "readBytes", "", "count", "remaining", "app_debug"})
public final class BitStreamReader {
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Bitmap bitmap = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.hexglyph.steganography.RandomPixelMapper.SlotAddress> slots = null;
    private int slotIndex = 0;
    
    public BitStreamReader(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    java.util.List<com.hexglyph.steganography.RandomPixelMapper.SlotAddress> slots) {
        super();
    }
    
    /**
     * Read [count] bytes (MSB first within each byte).
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] readBytes(int count) {
        return null;
    }
    
    /**
     * Read a single bit (0 or 1) from the next available slot.
     */
    public final int readBit() {
        return 0;
    }
    
    /**
     * Remaining readable slots.
     */
    public final int remaining() {
        return 0;
    }
}