package com.hexglyph.steganography

import android.graphics.Bitmap

/**
 * BitStreamReader
 *
 * Extracts bits from the least-significant bits of the nominated pixel
 * channels in [bitmap], following the same [slots] address sequence as the
 * corresponding [BitStreamWriter].
 */
class BitStreamReader(
    private val bitmap: Bitmap,
    private val slots:  List<RandomPixelMapper.SlotAddress>
) {
    private var slotIndex = 0

    /** Read [count] bytes (MSB first within each byte). */
    fun readBytes(count: Int): ByteArray {
        val result = ByteArray(count)
        for (i in 0 until count) {
            var value = 0
            for (bitPos in 7 downTo 0) {
                value = (value shl 1) or readBit()
            }
            result[i] = value.toByte()
        }
        return result
    }

    /** Read a single bit (0 or 1) from the next available slot. */
    fun readBit(): Int {
        check(slotIndex < slots.size) { "BitStreamReader: slot overflow at index $slotIndex" }
        val slot  = slots[slotIndex++]
        val pixel = bitmap.getPixel(slot.x, slot.y)
        return when (slot.channel) {
            0 -> (pixel ushr 16) and 1
            1 -> (pixel ushr 8)  and 1
            else -> pixel        and 1
        }
    }

    /** Remaining readable slots. */
    fun remaining(): Int = slots.size - slotIndex
}
