package com.hexglyph.ui.screens;

import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.input.VisualTransformation;
import com.hexglyph.utils.SecureClipboard;
import com.hexglyph.viewmodel.StealthViewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/hexglyph/ui/screens/PendingAction;", "", "(Ljava/lang/String;I)V", "None", "Inspect", "Decode", "app_debug"})
enum PendingAction {
    /*public static final*/ None /* = new None() */,
    /*public static final*/ Inspect /* = new Inspect() */,
    /*public static final*/ Decode /* = new Decode() */;
    
    PendingAction() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.hexglyph.ui.screens.PendingAction> getEntries() {
        return null;
    }
}