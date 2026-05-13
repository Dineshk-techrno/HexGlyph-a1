package com.hexglyph.ui;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.navigation.compose.*;
import com.hexglyph.utils.RootDetector;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\t\n\u000b\fB\u0017\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u0082\u0001\u0004\r\u000e\u000f\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/hexglyph/ui/Screen;", "", "route", "", "label", "(Ljava/lang/String;Ljava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "getRoute", "Decode", "Encode", "StealthDecode", "StealthEncode", "Lcom/hexglyph/ui/Screen$Decode;", "Lcom/hexglyph/ui/Screen$Encode;", "Lcom/hexglyph/ui/Screen$StealthDecode;", "Lcom/hexglyph/ui/Screen$StealthEncode;", "app_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    
    private Screen(java.lang.String route, java.lang.String label) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/ui/Screen$Decode;", "Lcom/hexglyph/ui/Screen;", "()V", "app_debug"})
    public static final class Decode extends com.hexglyph.ui.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.hexglyph.ui.Screen.Decode INSTANCE = null;
        
        private Decode() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/ui/Screen$Encode;", "Lcom/hexglyph/ui/Screen;", "()V", "app_debug"})
    public static final class Encode extends com.hexglyph.ui.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.hexglyph.ui.Screen.Encode INSTANCE = null;
        
        private Encode() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/ui/Screen$StealthDecode;", "Lcom/hexglyph/ui/Screen;", "()V", "app_debug"})
    public static final class StealthDecode extends com.hexglyph.ui.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.hexglyph.ui.Screen.StealthDecode INSTANCE = null;
        
        private StealthDecode() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/hexglyph/ui/Screen$StealthEncode;", "Lcom/hexglyph/ui/Screen;", "()V", "app_debug"})
    public static final class StealthEncode extends com.hexglyph.ui.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.hexglyph.ui.Screen.StealthEncode INSTANCE = null;
        
        private StealthEncode() {
        }
    }
}