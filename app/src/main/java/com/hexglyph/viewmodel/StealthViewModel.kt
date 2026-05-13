package com.hexglyph.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexglyph.crypto.KeyDerivation
import com.hexglyph.crypto.KeystoreManager
import com.hexglyph.database.GlyphDao
import com.hexglyph.database.GlyphEntity
import com.hexglyph.renderer.GlyphExporter
import com.hexglyph.steganography.StealthEncoder
import com.hexglyph.steganography.StealthScanEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StealthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stealthEncoder:  StealthEncoder,
    private val stealthScanEngine: StealthScanEngine,
    private val keystoreManager: KeystoreManager,
    private val glyphExporter:   GlyphExporter,
    private val glyphDao:        GlyphDao
) : ViewModel() {

    // ── Shared state ──────────────────────────────────────────────────────────

    sealed class StealthState {
        object Idle       : StealthState()
        object Loading    : StealthState()
        data class EncodeSuccess(val bitmap: Bitmap, val shareUri: Uri) : StealthState()
        data class DecodeSuccess(val plaintext: String)                  : StealthState()
        data class HasPayload(val uri: Uri)                              : StealthState()
        data class Error(val message: String)                            : StealthState()
    }

    private val _state = MutableStateFlow<StealthState>(StealthState.Idle)
    val state: StateFlow<StealthState> = _state.asStateFlow()

    // ── Encode fields ─────────────────────────────────────────────────────────

    private val _plaintext       = MutableStateFlow("")
    val plaintext: StateFlow<String> = _plaintext.asStateFlow()

    private val _groupCode       = MutableStateFlow(keystoreManager.loadGroupCode() ?: "")
    val groupCode: StateFlow<String> = _groupCode.asStateFlow()

    private val _groupProtected  = MutableStateFlow(false)
    val groupProtected: StateFlow<Boolean> = _groupProtected.asStateFlow()

    private val _carrierUri      = MutableStateFlow<Uri?>(null)
    val carrierUri: StateFlow<Uri?> = _carrierUri.asStateFlow()

    val charCount: StateFlow<Int> = _plaintext
        .map { it.toByteArray(Charsets.UTF_8).size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    // ── Decode fields ─────────────────────────────────────────────────────────

    private val _decodeGroupCode = MutableStateFlow(keystoreManager.loadGroupCode() ?: "")
    val decodeGroupCode: StateFlow<String> = _decodeGroupCode.asStateFlow()

    // ── Actions: setters ──────────────────────────────────────────────────────

    fun onPlaintextChange(text: String)          { _plaintext.value = text }
    fun onGroupCodeChange(code: String)          {
        _groupCode.value = code
        keystoreManager.saveGroupCode(code)
    }
    fun onGroupProtectedChange(enabled: Boolean) { _groupProtected.value = enabled }
    fun onCarrierUriSelected(uri: Uri?)          { _carrierUri.value = uri }
    fun onDecodeGroupCodeChange(code: String)    {
        _decodeGroupCode.value = code
        keystoreManager.saveGroupCode(code)
    }
    fun reset() { _state.value = StealthState.Idle }

    // ── Actions: encode ───────────────────────────────────────────────────────

    fun encodeStealthGlyph() {
        val text = _plaintext.value.trim()
        val code = _groupCode.value.trim()

        if (text.isEmpty()) { _state.value = StealthState.Error("Message cannot be empty"); return }
        if (code.isEmpty()) { _state.value = StealthState.Error("Group code cannot be empty"); return }
        if (text.toByteArray(Charsets.UTF_8).size > 197) {
            _state.value = StealthState.Error("Message too long (max 197 bytes)")
            return
        }

        _state.value = StealthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = stealthEncoder.encode(
                    context        = context,
                    plaintext      = text,
                    groupCode      = code,
                    groupProtected = _groupProtected.value,
                    carrierUri     = _carrierUri.value
                )
                val uri = glyphExporter.exportPng(
                    context  = context,
                    bitmap   = bitmap,
                    filename = "stealth_${System.currentTimeMillis()}.png"
                )

                glyphDao.insert(
                    GlyphEntity(
                        direction     = "sent_stealth",
                        plaintext     = text,
                        groupCodeHint = code.take(3) + "***",
                        filePath      = uri.path,
                        epochDay      = KeyDerivation.currentEpochDay()
                    )
                )

                _state.value = StealthState.EncodeSuccess(bitmap, uri)
            } catch (e: Exception) {
                _state.value = StealthState.Error(e.message ?: "Stealth encoding failed")
            }
        }
    }

    // ── Actions: detect ───────────────────────────────────────────────────────

    fun detectAndDecodeFromUri(uri: Uri) {
        val code = _decodeGroupCode.value.trim()
        if (code.isEmpty()) { _state.value = StealthState.Error("Enter group code first"); return }

        _state.value = StealthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plaintext = stealthScanEngine.decodeFromUri(context, uri, code)
                    ?: throw IllegalStateException("Could not load image from selected file.")

                glyphDao.insert(
                    GlyphEntity(
                        direction     = "received_stealth",
                        plaintext     = plaintext,
                        groupCodeHint = code.take(3) + "***",
                        epochDay      = KeyDerivation.currentEpochDay()
                    )
                )

                _state.value = StealthState.DecodeSuccess(plaintext)
            } catch (e: Exception) {
                _state.value = StealthState.Error(e.message ?: "Stealth decode failed")
            }
        }
    }

    /** Quick check — does the image at [uri] contain a hidden glyph? */
    fun inspectUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val hasPayload = stealthScanEngine.hasStealthPayloadInUri(context, uri)
            _state.value = if (hasPayload) StealthState.HasPayload(uri)
                           else StealthState.Error("No hidden HexGlyph payload found in this image.")
        }
    }

    // ── Share helper ──────────────────────────────────────────────────────────

    fun shareStealthImage(uri: Uri, context: Context) {
        context.startActivity(
            android.content.Intent.createChooser(
                glyphExporter.shareIntent(uri),
                "Share Stealth Glyph"
            ).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
