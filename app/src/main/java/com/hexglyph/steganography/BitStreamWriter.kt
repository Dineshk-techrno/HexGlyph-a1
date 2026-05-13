package com.hexglyph.steganography

import android.graphics.Bitmap

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
class BitStreamWriter(
    private val bitmap: Bitmap,
    private val slots:  List<RandomPixelMapper.SlotAddress>
) {
    private var slotIndex = 0

    /** Write all bytes in [data] — MSB first within each byte. */
    fun writeBytes(data: ByteArray) {
        for (byte in data) {
            for (bitPos in 7 downTo 0) {
                val bit = (byte.toInt() ushr bitPos) and 1
                writeBit(bit)
            }
        }
    }

    /** Write a single bit (0 or 1) into the next available slot. */
    fun writeBit(bit: Int) {
        check(slotIndex < slots.size) { "BitStreamWriter: slot overflow at index $slotIndex" }
        val slot  = slots[slotIndex++]
        val pixel = bitmap.getPixel(slot.x, slot.y)
        val a     = (pixel ushr 24) and 0xFF
        val r     = (pixel ushr 16) and 0xFF
        val g     = (pixel ushr 8)  and 0xFF
        val b     =  pixel          and 0xFF

        val newPixel = when (slot.channel) {
            0 -> android.graphics.Color.argb(a, (r and 0xFE) or bit, g, b)
            1 -> android.graphics.Color.argb(a, r, (g and 0xFE) or bit, b)
            else -> android.graphics.Color.argb(a, r, g, (b and 0xFE) or bit)
        }
        bitmap.setPixel(slot.x, slot.y, newPixel)
    }

    /** How many slots remain. */
    fun remainingCapacity(): Int = slots.size - slotIndex
}
