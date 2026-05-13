package com.hexglyph.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexglyph.crypto.CryptoEngine
import com.hexglyph.crypto.KeyDerivation
import com.hexglyph.crypto.KeystoreManager
import com.hexglyph.database.GlyphDao
import com.hexglyph.database.GlyphEntity
import com.hexglyph.scanner.ScanEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecodeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoEngine: CryptoEngine,
    private val keystoreManager: KeystoreManager,
    private val scanEngine: ScanEngine,
    private val glyphDao: GlyphDao
) : ViewModel() {

    // ── UI State ──────────────────────────────────────────────────────────────

    sealed class DecodeState {
        object Idle       : DecodeState()
        object Scanning   : DecodeState()
        object Processing : DecodeState()
        data class Success(val plaintext: String) : DecodeState()
        data class Error(val message: String) : DecodeState()
    }

    private val _state = MutableStateFlow<DecodeState>(DecodeState.Idle)
    val state: StateFlow<DecodeState> = _state.asStateFlow()

    private val _groupCode = MutableStateFlow(keystoreManager.loadGroupCode() ?: "")
    val groupCode: StateFlow<String> = _groupCode.asStateFlow()

    // ── Camera frame callback ─────────────────────────────────────────────────

    /**
     * Called by ScanEngine with raw glyph bytes from the camera frame.
     * Triggers decode if we're in Scanning state.
     */
    fun onGlyphBytesDetected(glyphBytes: ByteArray) {
        if (_state.value !is DecodeState.Scanning) return
        val code = _groupCode.value.trim()
        if (code.isEmpty()) return

        _state.value = DecodeState.Processing
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plainBytes = cryptoEngine.decodeFull(glyphBytes, code)
                val plaintext  = plainBytes.toString(Charsets.UTF_8)
                saveToHistory(plaintext, code)
                _state.value = DecodeState.Success(plaintext)
            } catch (e: Exception) {
                _state.value = DecodeState.Error("Decode failed: ${e.message}")
            }
        }
    }

    // ── File/URI decode ───────────────────────────────────────────────────────

    fun decodeFromUri(uri: Uri) {
        val code = _groupCode.value.trim()
        if (code.isEmpty()) { _state.value = DecodeState.Error("Enter group code first"); return }

        _state.value = DecodeState.Processing
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val glyphBytes = scanEngine.scanFromUri(context, uri)
                    ?: throw IllegalStateException("Could not extract glyph data from image")
                val plainBytes = cryptoEngine.decodeFull(glyphBytes, code)
                val plaintext  = plainBytes.toString(Charsets.UTF_8)
                saveToHistory(plaintext, code)
                _state.value = DecodeState.Success(plaintext)
            } catch (e: Exception) {
                _state.value = DecodeState.Error(e.message ?: "Decode failed")
            }
        }
    }

    // ── State transitions ─────────────────────────────────────────────────────

    fun startScanning()   { _state.value = DecodeState.Scanning }
    fun reset()           { _state.value = DecodeState.Idle }
    fun onGroupCodeChange(code: String) {
        _groupCode.value = code
        keystoreManager.saveGroupCode(code)
    }

    // ── History ───────────────────────────────────────────────────────────────

    private suspend fun saveToHistory(plaintext: String, code: String) {
        glyphDao.insert(
            GlyphEntity(
                direction     = "received",
                plaintext     = plaintext,
                groupCodeHint = code.take(3) + "***",
                epochDay      = KeyDerivation.currentEpochDay()
            )
        )
    }
}
