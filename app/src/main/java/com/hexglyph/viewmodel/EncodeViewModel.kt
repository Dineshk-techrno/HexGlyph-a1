package com.hexglyph.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexglyph.crypto.CryptoEngine
import com.hexglyph.crypto.KeyDerivation
import com.hexglyph.crypto.KeystoreManager
import com.hexglyph.database.GlyphDao
import com.hexglyph.database.GlyphEntity
import com.hexglyph.renderer.GlyphExporter
import com.hexglyph.renderer.GlyphRenderer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EncodeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoEngine: CryptoEngine,
    private val keystoreManager: KeystoreManager,
    private val glyphRenderer: GlyphRenderer,
    private val glyphExporter: GlyphExporter,
    private val glyphDao: GlyphDao
) : ViewModel() {

    // ── UI State ──────────────────────────────────────────────────────────────

    sealed class EncodeState {
        object Idle      : EncodeState()
        object Loading   : EncodeState()
        data class Success(val bitmap: Bitmap, val shareUri: Uri) : EncodeState()
        data class Error(val message: String) : EncodeState()
    }

    private val _state = MutableStateFlow<EncodeState>(EncodeState.Idle)
    val state: StateFlow<EncodeState> = _state.asStateFlow()

    private val _plaintext  = MutableStateFlow("")
    val plaintext: StateFlow<String> = _plaintext.asStateFlow()

    private val _groupCode  = MutableStateFlow(keystoreManager.loadGroupCode() ?: "")
    val groupCode: StateFlow<String> = _groupCode.asStateFlow()

    val charCount: StateFlow<Int> = _plaintext
        .map { it.toByteArray(Charsets.UTF_8).size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    // ── Actions ───────────────────────────────────────────────────────────────

    fun onPlaintextChange(text: String) { _plaintext.value = text }

    fun onGroupCodeChange(code: String) {
        _groupCode.value = code
        keystoreManager.saveGroupCode(code)
    }

    fun encode() {
        val text = _plaintext.value.trim()
        val code = _groupCode.value.trim()

        if (text.isEmpty()) { _state.value = EncodeState.Error("Message cannot be empty"); return }
        if (code.isEmpty()) { _state.value = EncodeState.Error("Group code cannot be empty"); return }

        val bytes = text.toByteArray(Charsets.UTF_8)
        if (bytes.size > 197) {
            _state.value = EncodeState.Error("Message too long (${bytes.size}/197 bytes)")
            return
        }

        _state.value = EncodeState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val glyphData = cryptoEngine.encodeFull(bytes, code)
                val bitmap    = glyphRenderer.render(glyphData)
                val uri       = glyphExporter.exportPng(context, bitmap)

                // Persist to history
                glyphDao.insert(
                    GlyphEntity(
                        direction    = "sent",
                        plaintext    = text,
                        groupCodeHint = code.take(3) + "***",
                        filePath     = uri.path,
                        epochDay     = KeyDerivation.currentEpochDay()
                    )
                )

                _state.value = EncodeState.Success(bitmap, uri)
            } catch (e: Exception) {
                _state.value = EncodeState.Error(e.message ?: "Encoding failed")
            }
        }
    }

    fun reset() { _state.value = EncodeState.Idle }

    fun shareGlyph(uri: Uri, context: Context) {
        context.startActivity(
            android.content.Intent.createChooser(
                glyphExporter.shareIntent(uri),
                "Share HexGlyph"
            ).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
