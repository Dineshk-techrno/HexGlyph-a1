package com.hexglyph.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexglyph.scanner.ScanEngine
import com.hexglyph.utils.SecureClipboard
import com.hexglyph.viewmodel.DecodeViewModel

private val OrangeAccent = Color(0xFFFF6B35)

@Composable
fun DecodeScreen(vm: DecodeViewModel = hiltViewModel()) {
    val state          by vm.state.collectAsState()
    val groupCode      by vm.groupCode.collectAsState()
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState    = rememberScrollState()

    var showGroupCode  by remember { mutableStateOf(false) }
    var cameraStarted  by remember { mutableStateOf(false) }

    val scanEngine     = remember { ScanEngine() }

    // Camera permission
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted; if (granted) cameraStarted = true }

    // Image picker for file-based decode
    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { vm.decodeFromUri(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text("HEXGLYPH", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
        Text("Scan & decode a glyph", fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Camera preview / result area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0F0F1A)),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is DecodeViewModel.DecodeState.Scanning -> {
                    if (hasCameraPermission) {
                        AndroidView(
                            factory = { ctx ->
                                PreviewView(ctx).also { previewView ->
                                    scanEngine.startCamera(
                                        context        = ctx,
                                        lifecycleOwner = lifecycleOwner,
                                        previewSurfaceProvider = previewView.surfaceProvider,
                                        onGlyphDetected = vm::onGlyphBytesDetected
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        // Viewfinder overlay
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CropFree, contentDescription = "Scan area",
                                tint = OrangeAccent, modifier = Modifier.size(200.dp))
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Camera, contentDescription = null,
                                tint = Color(0xFFF0F0F0).copy(0.3f), modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Camera permission required",
                                color = Color(0xFFF0F0F0).copy(0.5f), fontSize = 13.sp)
                        }
                    }
                }

                is DecodeViewModel.DecodeState.Processing -> {
                    CircularProgressIndicator(color = OrangeAccent)
                    Spacer(Modifier.height(8.dp))
                    Text("Decoding…", color = Color(0xFFF0F0F0).copy(0.7f), fontSize = 13.sp)
                }

                is DecodeViewModel.DecodeState.Success -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null,
                            tint = Color(0xFF4CAF50), modifier = Modifier.size(40.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Decoded Successfully",
                            fontWeight = FontWeight.Bold, color = Color(0xFFF0F0F0))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text     = s.plaintext,
                            color    = Color(0xFFF0F0F0),
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is DecodeViewModel.DecodeState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(s.message, color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp)
                    }
                }

                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null,
                            tint = Color(0xFFF0F0F0).copy(0.3f), modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Point camera at a HexGlyph",
                            color = Color(0xFFF0F0F0).copy(0.4f), fontSize = 13.sp)
                    }
                }
            }
        }

        // Group code
        OutlinedTextField(
            value         = groupCode,
            onValueChange = vm::onGroupCodeChange,
            label         = { Text("Group Code") },
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

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Scan with camera
            Button(
                onClick = {
                    permLauncher.launch(Manifest.permission.CAMERA)
                    vm.startScanning()
                },
                colors   = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                modifier = Modifier.weight(1f).height(52.dp),
                enabled  = state !is DecodeViewModel.DecodeState.Scanning &&
                           state !is DecodeViewModel.DecodeState.Processing
            ) {
                Icon(Icons.Default.Camera, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Scan", fontWeight = FontWeight.Bold)
            }

            // Load from gallery
            OutlinedButton(
                onClick  = { imagePicker.launch("image/*") },
                modifier = Modifier.weight(1f).height(52.dp),
                enabled  = state !is DecodeViewModel.DecodeState.Processing
            ) {
                Icon(Icons.Default.Image, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Gallery")
            }
        }

        // Copy & Reset (after success)
        if (state is DecodeViewModel.DecodeState.Success) {
            val decoded = (state as DecodeViewModel.DecodeState.Success).plaintext
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = {
                        SecureClipboard.copySecure(context, "HexGlyph", decoded)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Copy")
                }
                Button(
                    onClick  = vm::reset,
                    colors   = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Scan Again")
                }
            }
        }

        // Reset on error
        if (state is DecodeViewModel.DecodeState.Error) {
            OutlinedButton(
                onClick  = vm::reset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Try Again")
            }
        }
    }

    // Stop camera when leaving scanning state
    LaunchedEffect(state) {
        if (state !is DecodeViewModel.DecodeState.Scanning) {
            scanEngine.stopCamera()
        }
    }
}
