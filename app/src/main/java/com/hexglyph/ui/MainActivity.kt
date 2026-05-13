package com.hexglyph.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.hexglyph.ui.screens.DecodeScreen
import com.hexglyph.ui.screens.EncodeScreen
import com.hexglyph.ui.screens.StealthDecodeScreen
import com.hexglyph.ui.screens.StealthEncodeScreen
import com.hexglyph.ui.theme.HexGlyphTheme
import com.hexglyph.utils.RootDetector
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String) {
    object Encode        : Screen("encode",         "Encode")
    object Decode        : Screen("decode",         "Decode")
    object StealthEncode : Screen("stealth_encode", "Stealth")
    object StealthDecode : Screen("stealth_detect", "Detect")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (RootDetector.isRooted(this)) {
            android.widget.Toast.makeText(
                this,
                "⚠️ Rooted device detected — security model may be compromised",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            HexGlyphTheme {
                HexGlyphApp()
            }
        }
    }
}

@Composable
fun HexGlyphApp() {
    val navController = rememberNavController()
    val currentEntry  by navController.currentBackStackEntryAsState()

    val items = listOf(
        Screen.Encode,
        Screen.Decode,
        Screen.StealthEncode,
        Screen.StealthDecode
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    is Screen.Encode        -> Icons.Default.Lock
                                    is Screen.Decode        -> Icons.Default.Camera
                                    is Screen.StealthEncode -> Icons.Default.Visibility
                                    is Screen.StealthDecode -> Icons.Default.Search
                                },
                                contentDescription = screen.label
                            )
                        },
                        label    = { Text(screen.label) },
                        selected = currentEntry?.destination?.hierarchy
                            ?.any { it.route == screen.route } == true,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Encode.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Encode.route)        { EncodeScreen() }
            composable(Screen.Decode.route)        { DecodeScreen() }
            composable(Screen.StealthEncode.route) { StealthEncodeScreen() }
            composable(Screen.StealthDecode.route) { StealthDecodeScreen() }
        }
    }
}
