package com.hexglyph.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexglyph.ui.components.GlyphCanvas
import com.hexglyph.viewmodel.EncodeViewModel

private val OrangeAccent = Color(0xFFFF6B35)

@Composable
fun EncodeScreen(vm: EncodeViewModel = hiltViewModel()) {
    val state      by vm.state.collectAsState()
    val plaintext  by vm.plaintext.collectAsState()
    val groupCode  by vm.groupCode.collectAsState()
    val charCount  by vm.charCount.collectAsState()
    val context    = LocalContext.current
    val scrollState = rememberScrollState()

    var showGroupCode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text       = "HEXGLYPH",
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            color      = OrangeAccent
        )
        Text(
            text     = "Encode a secure visual cipher",
            fontSize = 13.sp,
            color    = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Glyph preview
        GlyphCanvas(
            modifier  = Modifier
                .fillMaxWidth()
                .heightIn(max = 340.dp),
            bitmap    = (state as? EncodeViewModel.EncodeState.Success)?.bitmap,
            isLoading = state is EncodeViewModel.EncodeState.Loading
        )

        // Group code input
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
                        imageVector = if (showGroupCode) Icons.Default.VisibilityOff
                                      else Icons.Default.Visibility,
                        contentDescription = "Toggle visibility"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Message input
        OutlinedTextField(
            value         = plaintext,
            onValueChange = vm::onPlaintextChange,
            label         = { Text("Message") },
            placeholder   = { Text("Enter your secret message (max 197 bytes)") },
            minLines      = 3,
            maxLines      = 6,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (charCount > 180) Text("$charCount / 197 bytes",
                        color = if (charCount > 197) MaterialTheme.colorScheme.error else OrangeAccent)
                    else Text("$charCount / 197 bytes")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Error message
        if (state is EncodeViewModel.EncodeState.Error) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer)
                    Text(
                        text  = (state as EncodeViewModel.EncodeState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Encode button
        Button(
            onClick   = vm::encode,
            enabled   = state !is EncodeViewModel.EncodeState.Loading,
            colors    = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
            modifier  = Modifier.fillMaxWidth().height(52.dp)
        ) {
            if (state is EncodeViewModel.EncodeState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color    = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Encode Glyph", fontWeight = FontWeight.Bold)
            }
        }

        // Share / Reset (shown after success)
        if (state is EncodeViewModel.EncodeState.Success) {
            val uri = (state as EncodeViewModel.EncodeState.Success).shareUri
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
                    onClick  = { vm.shareGlyph(uri, context) },
                    colors   = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Share")
                }
            }
        }

        // Key rotation warning
        Card(
            colors   = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null,
                    tint = OrangeAccent, modifier = Modifier.size(16.dp))
                Text(
                    text     = "Keys rotate at 00:00 UTC — glyphs expire at midnight.",
                    fontSize = 11.sp,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
