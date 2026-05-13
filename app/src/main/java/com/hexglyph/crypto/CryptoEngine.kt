package com.hexglyph.crypto

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CryptoEngine @Inject constructor(private val keystoreManager: KeystoreManager) {

    companion object {
        private val MAGIC               = byteArrayOf(0x48, 0x47, 0x4C, 0x59)
        private const val VERSION: Byte = 0x01
        private const val MAX_PLAINTEXT = 197
        private const val DATA_CAPACITY = 305
    }

    private val secureRandom = SecureRandom()

    fun encodeFull(plaintext: ByteArray, groupCode: String): ByteArray {
        require(plaintext.size <= MAX_PLAINTEXT) {
            "Plaintext too long: ${plaintext.size} bytes (max $MAX_PLAINTEXT)"
        }
        val gcmIv       = ByteArray(12).also { secureRandom.nextBytes(it) }
        val chachaNonce  = ByteArray(12).also { secureRandom.nextBytes(it) }
        val ctrNonce     = ByteArray(16).also { secureRandom.nextBytes(it) }
        val k1 = KeyDerivation.deriveKeys(groupCode, gcmIv).first
        val k2 = KeyDerivation.deriveKeys(groupCode, gcmIv).second
        val timestamp  = System.currentTimeMillis().toBytesBigEndian()
        val ptLen      = plaintext.size.toShortBigEndian()
        val plainBlock = MAGIC + byteArrayOf(VERSION) + timestamp + gcmIv + ptLen + plaintext
        val encCipher  = Cipher.getInstance("AES/GCM/NoPadding")
        encCipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(k1, "AES"), GCMParameterSpec(128, gcmIv))
        val aesGcmOut  = encCipher.doFinal(plainBlock)
        val ciphertext = aesGcmOut.copyOf(aesGcmOut.size - 16)
        val authTag    = aesGcmOut.copyOfRange(aesGcmOut.size - 16, aesGcmOut.size)
        val afterChacha = ChaCha20.xor(ciphertext, k2, chachaNonce)
        val afterCtr    = aesCtr(afterChacha, k1, ctrNonce, encrypt = true)
        val shuffled    = KeyDerivation.shuffleBytes(afterCtr, k1)
        val outputBlock = gcmIv + chachaNonce + ctrNonce + shuffled + authTag
        val rsEncoded   = ReedSolomon.encode(outputBlock)
        val obfuscated  = Obfuscator.obfuscate(rsEncoded, k1)
        return obfuscated.copyOf(DATA_CAPACITY)
    }

    fun decodeFull(glyphBytes: ByteArray, groupCode: String): ByteArray {
        require(glyphBytes.size >= DATA_CAPACITY) {
            "Glyph data too short: ${glyphBytes.size}, need $DATA_CAPACITY"
        }
        val gcmIv = glyphBytes.copyOf(12)
        val k1    = KeyDerivation.deriveKeys(groupCode, gcmIv).first
        val k2    = KeyDerivation.deriveKeys(groupCode, gcmIv).second
        val deobfuscated = Obfuscator.deobfuscate(glyphBytes.copyOf(DATA_CAPACITY), k1)
        val rsDecoded    = ReedSolomon.decode(deobfuscated)
        var offset       = 0
        val parsedGcmIv  = rsDecoded.copyOfRange(offset, offset + 12); offset += 12
        val chachaNonce  = rsDecoded.copyOfRange(offset, offset + 12); offset += 12
        val ctrNonce     = rsDecoded.copyOfRange(offset, offset + 16); offset += 16
        val shuffledData = rsDecoded.copyOfRange(offset, rsDecoded.size - 16)
        val authTag      = rsDecoded.copyOfRange(rsDecoded.size - 16, rsDecoded.size)
        val unshuffled    = KeyDerivation.unshuffleBytes(shuffledData, k1)
        val afterUnctr    = aesCtr(unshuffled, k1, ctrNonce, encrypt = false)
        val afterUnChacha = ChaCha20.xor(afterUnctr, k2, chachaNonce)
        val decCipher = Cipher.getInstance("AES/GCM/NoPadding")
        decCipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(k1, "AES"), GCMParameterSpec(128, parsedGcmIv))
        val plainBlock = decCipher.doFinal(afterUnChacha + authTag)
        val magic = plainBlock.copyOf(4)
        require(magic.contentEquals(MAGIC)) { "Invalid MAGIC — wrong group code or corrupt glyph" }
        val version = plainBlock[4]
        require(version == VERSION) { "Unsupported version: $version" }
        val ptLenOff = 5 + 8 + 12
        val ptLen    = ((plainBlock[ptLenOff].toInt() and 0xFF) shl 8) or
                        (plainBlock[ptLenOff + 1].toInt() and 0xFF)
        val ptStart  = ptLenOff + 2
        require(ptLen >= 0 && ptStart + ptLen <= plainBlock.size) { "Corrupt ptLen: $ptLen" }
        return plainBlock.copyOfRange(ptStart, ptStart + ptLen)
    }

    private fun aesCtr(data: ByteArray, key: ByteArray, nonce: ByteArray, encrypt: Boolean): ByteArray {
        val cipher = Cipher.getInstance("AES/CTR/NoPadding")
        val mode   = if (encrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE
        cipher.init(mode, SecretKeySpec(key, "AES"), javax.crypto.spec.IvParameterSpec(nonce))
        return cipher.doFinal(data)
    }

    private fun Long.toBytesBigEndian(): ByteArray = ByteArray(8) { i ->
        ((this shr (56 - i * 8)) and 0xFF).toByte()
    }

    private fun Int.toShortBigEndian(): ByteArray = byteArrayOf(
        ((this shr 8) and 0xFF).toByte(),
        (this and 0xFF).toByte()
    )

    private operator fun ByteArray.plus(other: ByteArray): ByteArray {
        val result = ByteArray(size + other.size)
        System.arraycopy(this, 0, result, 0, size)
        System.arraycopy(other, 0, result, size, other.size)
        return result
    }
}
