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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Visibility
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
import com.hexglyph.viewmodel.StealthViewModel

private val CyanAccent   = Color(0xFF00E5FF)
private val DarkSurface  = Color(0xFF0A0A14)
private val DarkCard     = Color(0xFF111122)

@Composable
fun StealthEncodeScreen(vm: StealthViewModel = hiltViewModel()) {
    val state          by vm.state.collectAsState()
    val plaintext      by vm.plaintext.collectAsState()
    val groupCode      by vm.groupCode.collectAsState()
    val groupProtected by vm.groupProtected.collectAsState()
    val carrierUri     by vm.carrierUri.collectAsState()
    val charCount      by vm.charCount.collectAsState()
    val context        = LocalContext.current
    val scrollState    = rememberScrollState()

    var showGroupCode  by remember { mutableStateOf(false) }

    // Image picker for carrier selection
    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> vm.onCarrierUriSelected(uri) }

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
                Icons.Default.Visibility,
                contentDescription = null,
                tint     = CyanAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text       = "STEALTH GLYPH",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = CyanAccent
                )
                Text(
                    text     = "Invisible encrypted payload inside an ordinary image",
                    fontSize = 11.sp,
                    color    = Color(0xFF80DEEA)
                )
            }
        }

        // ── Preview / result ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkCard)
                .border(1.dp, CyanAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is StealthViewModel.StealthState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = CyanAccent)
                        Spacer(Modifier.height(10.dp))
                        Text("Embedding hidden payload…",
                            color = CyanAccent.copy(0.7f), fontSize = 12.sp)
                    }
                }
                is StealthViewModel.StealthState.EncodeSuccess -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint     = Color(0xFF00E676),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Stealth Glyph created",
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFFF0F0F0)
                        )
                        Text(
                            "Payload hidden invisibly inside image",
                            fontSize = 11.sp,
                            color    = Color(0xFF80DEEA)
                        )
                    }
                }
                is StealthViewModel.StealthState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint     = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(s.message,
                            color    = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp)
                    }
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint     = CyanAccent.copy(0.3f),
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Output image will appear completely normal",
                            color    = Color(0xFFF0F0F0).copy(0.35f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // ── Carrier image selection ───────────────────────────────────────────
        Card(
            colors   = CardDefaults.cardColors(containerColor = DarkCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    "Carrier Image",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = CyanAccent
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick  = { imagePicker.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(
                            contentColor = CyanAccent
                        )
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Select Image", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick  = { vm.onCarrierUriSelected(null) },
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF80DEEA)
                        )
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Auto Select", fontSize = 12.sp)
                    }
                }
                if (carrierUri != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "✓ Custom carrier selected",
                        color    = Color(0xFF00E676),
                        fontSize = 11.sp
                    )
                } else {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Auto: app will select optimal carrier from internal pool",
                        color    = Color(0xFFF0F0F0).copy(0.4f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // ── Group code ────────────────────────────────────────────────────────
        OutlinedTextField(
            value         = groupCode,
            onValueChange = vm::onGroupCodeChange,
            label         = { Text("Group Code") },
            placeholder   = { Text("Shared secret") },
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
                focusedBorderColor   = CyanAccent,
                focusedLabelColor    = CyanAccent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Message ───────────────────────────────────────────────────────────
        OutlinedTextField(
            value         = plaintext,
            onValueChange = vm::onPlaintextChange,
            label         = { Text("Hidden Message") },
            placeholder   = { Text("Enter your secret message (max 197 bytes)") },
            minLines      = 3,
            maxLines      = 6,
            colors  = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanAccent,
                focusedLabelColor  = CyanAccent
            ),
            supportingText = {
                val over = charCount > 197
                Text(
                    "$charCount / 197 bytes",
                    color = when {
                        over          -> MaterialTheme.colorScheme.error
                        charCount > 180 -> CyanAccent
                        else            -> Color(0xFF80DEEA)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // ── Group Protection toggle ───────────────────────────────────────────
        Card(
            colors   = CardDefaults.cardColors(containerColor = DarkCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Group Protection",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFFF0F0F0)
                    )
                    Text(
                        "Adds an extra encryption layer using Group Code hash",
                        fontSize = 11.sp,
                        color    = Color(0xFFF0F0F0).copy(0.5f)
                    )
                }
                Switch(
                    checked         = groupProtected,
                    onCheckedChange = vm::onGroupProtectedChange,
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor  = CyanAccent,
                        checkedTrackColor  = CyanAccent.copy(0.4f)
                    )
                )
            }
        }

        // ── Encode button ─────────────────────────────────────────────────────
        Button(
            onClick  = vm::encodeStealthGlyph,
            enabled  = state !is StealthViewModel.StealthState.Loading,
            colors   = ButtonDefaults.buttonColors(containerColor = CyanAccent),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (state is StealthViewModel.StealthState.Loading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(22.dp),
                    color       = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Hide Stealth Glyph",
                    fontWeight = FontWeight.Bold,
                    color      = Color.Black
                )
            }
        }

        // ── Share / Reset after success ───────────────────────────────────────
        if (state is StealthViewModel.StealthState.EncodeSuccess) {
            val uri = (state as StealthViewModel.StealthState.EncodeSuccess).shareUri
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = vm::reset,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("New")
                }
                Button(
                    onClick  = { vm.shareStealthImage(uri, context) },
                    colors   = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(6.dp))
                    Text("Share Image", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ── Reset on error ────────────────────────────────────────────────────
        if (state is StealthViewModel.StealthState.Error) {
            OutlinedButton(onClick = vm::reset, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Try Again")
            }
        }

        // ── Security note ─────────────────────────────────────────────────────
        Card(
            colors   = CardDefaults.cardColors(containerColor = DarkCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null,
                    tint = CyanAccent, modifier = Modifier.size(14.dp))
                Text(
                    "Export as PNG only. Avoid JPEG / social media compression — " +
                    "lossy formats destroy hidden payload data.",
                    fontSize = 10.sp,
                    color    = Color(0xFF80DEEA).copy(0.7f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
