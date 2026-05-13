package com.hexglyph.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import java.io.File

// ── RootDetector ──────────────────────────────────────────────────────────────

/**
 * Heuristic root detection. Warning-only — cannot prevent a determined attacker
 * with physical access to a rooted device.
 */
object RootDetector {

    private val ROOT_BINARIES = listOf(
        "/system/app/Superuser.apk",
        "/sbin/su", "/system/bin/su", "/system/xbin/su",
        "/data/local/xbin/su", "/data/local/bin/su",
        "/system/sd/xbin/su", "/system/bin/failsafe/su",
        "/data/local/su", "/su/bin/su"
    )

    private val ROOT_PACKAGES = listOf(
        "com.topjohnwu.magisk",
        "com.noshufou.android.su",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.zachspong.temprootremovejb",
        "com.ramdroid.appquarantine"
    )

    fun isRooted(context: Context): Boolean =
        checkRootBinaries() || checkBuildTags() || checkRootPackages(context)

    private fun checkRootBinaries(): Boolean =
        ROOT_BINARIES.any { File(it).exists() }

    private fun checkBuildTags(): Boolean =
        android.os.Build.TAGS?.contains("test-keys") == true

    private fun checkRootPackages(context: Context): Boolean {
        val pm = context.packageManager
        return ROOT_PACKAGES.any { pkg ->
            try { pm.getPackageInfo(pkg, 0); true } catch (_: Exception) { false }
        }
    }
}

// ── SecureClipboard ───────────────────────────────────────────────────────────

/**
 * Auto-clears sensitive clipboard content after 60 seconds.
 */
object SecureClipboard {

    private const val CLEAR_DELAY_MS = 60_000L
    private val handler = Handler(Looper.getMainLooper())

    fun copySecure(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        scheduleClear(clipboard)
    }

    private fun scheduleClear(clipboard: ClipboardManager) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
        }, CLEAR_DELAY_MS)
    }

    fun cancelPendingClear() {
        handler.removeCallbacksAndMessages(null)
    }
}
