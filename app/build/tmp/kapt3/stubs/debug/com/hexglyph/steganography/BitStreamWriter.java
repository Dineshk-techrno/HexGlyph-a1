package com.hexglyph.steganography;

import android.graphics.Bitmap;

/**
 * BitStreamWriter
 *
 * Writes a stream of bits into the least-significant bits of the nominated
 * pixel channels in [bitmap] according to the [slots] address sequence
 * produced by [RandomPixelMapper].
 *
 * Only the LSB of each R, G, or B channel is modified — the visual delta
 * is at most 1 intensity unit and is imperceptible to human vision.
 *
 * The bitmap MUST be mutable (Bitmap.Config.ARGB_8888 or similar).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\n\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/hexglyph/steganography/BitStreamWriter;", "", "bitmap", "Landroid/graphics/Bitmap;", "slots", "", "Lcom/hexglyph/steganography/RandomPixelMapper$SlotAddress;", "(Landroid/graphics/Bitmap;Ljava/util/List;)V", "slotIndex", "", "remainingCapacity", "writeBit", "", "bit", "writeBytes", "data", "", "app_debug"})
public final class BitStreamWriter {
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Bitmap bitmap = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.hexglyph.steganography.RandomPixelMapper.SlotAddress> slots = null;
    private int slotIndex = 0;
    
    public BitStreamWriter(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    java.util.List<com.hexglyph.steganography.RandomPixelMapper.SlotAddress> slots) {
        super();
    }
    
    /**
     * Write all bytes in [data] — MSB first within each byte.
     */
    public final void writeBytes(@org.jetbrains.annotations.NotNull()
    byte[] data) {
    }
    
    /**
     * Write a single bit (0 or 1) into the next available slot.
     */
    public final void writeBit(int bit) {
    }
    
    /**
     * How many slots remain.
     */
    public final int remainingCapacity() {
        return 0;
    }
}