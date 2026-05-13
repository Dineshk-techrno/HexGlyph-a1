package com.hexglyph.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexglyph.utils.SecureClipboard
import com.hexglyph.viewmodel.StealthViewModel

private val CyanAccent  = Color(0xFF00E5FF)
private val DarkSurface = Color(0xFF0A0A14)
private val DarkCard    = Color(0xFF111122)

@Composable
fun StealthDecodeScreen(vm: StealthViewModel = hiltViewModel()) {
    val state         by vm.state.collectAsState()
    val decodeCode    by vm.decodeGroupCode.collectAsState()
    val context       = LocalContext.current
    val scrollState   = rememberScrollState()

    var showGroupCode by remember { mutableStateOf(false) }

    // Single picker used for both quick-inspect and full decode
    var pendingAction by remember { mutableStateOf<PendingAction>(PendingAction.None) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        when (pendingAction) {
            PendingAction.Inspect -> vm.inspectUri(uri)
            PendingAction.Decode  -> vm.detectAndDecodeFromUri(uri)
            PendingAction.None    -> { /* no-op */ }
        }
        pendingAction = PendingAction.None
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSurface)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint     = CyanAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    "DETECT STEALTH GLYPH",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = CyanAccent
                )
                Text(
                    "Reveal hidden payloads inside ordinary images",
                    fontSize = 11.sp,
                    color    = Color(0xFF80DEEA)
                )
            }
        }

        // ── Result box ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkCard)
                .border(1.dp, CyanAccent.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is StealthViewModel.StealthState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        CircularProgressIndicator(color = CyanAccent)
                        Spacer(Modifier.height(12.dp))
                        Text("Scanning image for hidden payload…",
                            color = CyanAccent.copy(0.7f), fontSize = 12.sp)
                    }
                }

                is StealthViewModel.StealthState.HasPayload -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null,
                            tint = CyanAccent, modifier = Modifier.size(44.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Hidden HexGlyph payload detected!",
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFFF0F0F0)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Enter group code below and tap Decode to reveal.",
                            fontSize = 12.sp,
                            color    = Color(0xFF80DEEA)
                        )
                    }
                }

                is StealthViewModel.StealthState.DecodeSuccess -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null,
                                tint = Color(0xFF00E676), modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Hidden message revealed",
                                fontWeight = FontWeight.Bold,
                                color      = Color(0xFF00E676),
                                fontSize   = 14.sp
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Card(
                            colors   = CardDefaults.cardColors(
                                containerColor = Color(0xFF001A1A)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text     = s.plaintext,
                                color    = Color(0xFFF0F0F0),
                                fontSize = 15.sp,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }

                is StealthViewModel.StealthState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(36.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(s.message,
                            color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                }

                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null,
                            tint = CyanAccent.copy(0.25f), modifier = Modifier.size(52.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Load an image to scan for hidden payloads",
                            color    = Color(0xFFF0F0F0).copy(0.3f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // ── Group code ────────────────────────────────────────────────────────
        OutlinedTextField(
            value         = decodeCode,
            onValueChange = vm::onDecodeGroupCodeChange,
            label         = { Text("Group Code") },
            placeholder   = { Text("Required to decrypt hidden payload") },
            singleLine    = true,
            visualTransformation = if (showGroupCode) VisualTransformation.None
                                   else PasswordVisualTransformation(),
            trailingIcon  = {
                IconButton(onClick = { showGroupCode = !showGroupCode }) {
                    Icon(
                        if (showGroupCode) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = "Toggle visibility"
                    )
                }
            },
            colors  = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanAccent,
                focusedLabelColor  = CyanAccent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Action buttons ────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Quick inspect
            OutlinedButton(
                onClick = {
                    pendingAction = PendingAction.Inspect
                    imagePicker.launch("image/*")
                },
                modifier = Modifier.weight(1f).height(52.dp),
                enabled  = state !is StealthViewModel.StealthState.Loading,
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent)
            ) {
                Icon(Icons.Default.Search, contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Inspect", fontSize = 13.sp)
            }

            // Full decode
            Button(
                onClick = {
                    pendingAction = PendingAction.Decode
                    imagePicker.launch("image/*")
                },
                modifier = Modifier.weight(1f).height(52.dp),
                enabled  = state !is StealthViewModel.StealthState.Loading,
                colors   = ButtonDefaults.buttonColors(containerColor = CyanAccent)
            ) {
                Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color.Black,
                    modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Decode", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 13.sp)
            }
        }

        // ── Copy & Reset after success ────────────────────────────────────────
        if (state is StealthViewModel.StealthState.DecodeSuccess) {
            val decoded = (state as StealthViewModel.StealthState.DecodeSuccess).plaintext
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick  = { SecureClipboard.copySecure(context, "HexGlyph", decoded) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Copy")
                }
                Button(
                    onClick  = vm::reset,
                    colors   = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(6.dp))
                    Text("Scan Again", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (state is StealthViewModel.StealthState.Error ||
            state is StealthViewModel.StealthState.HasPayload) {
            OutlinedButton(onClick = vm::reset, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Reset")
            }
        }

        // ── Compression warning ───────────────────────────────────────────────
        Card(
            colors   = CardDefaults.cardColors(containerColor = DarkCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null,
                    tint = Color(0xFFFFCC00), modifier = Modifier.size(14.dp))
                Text(
                    "Only PNG images are supported. JPEG compression, WhatsApp, or " +
                    "social media recompression will destroy hidden payload data.",
                    fontSize = 10.sp,
                    color    = Color(0xFFFFCC00).copy(0.8f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

private enum class PendingAction { None, Inspect, Decode }
