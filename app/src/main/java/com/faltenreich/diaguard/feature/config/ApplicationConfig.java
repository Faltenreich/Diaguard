package com.faltenreich.diaguard.feature.config;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.BuildConfig;

public class ApplicationConfig {

    @Nullable
    private static ApplicationFlavor getFlavor() {
        return ApplicationFlavor.fromIdentifier(BuildConfig.FLAVOR);
    }

    public static boolean importDemoData() {
        return getFlavor() == ApplicationFlavor.DEMO;
    }

    public static boolean isCalculatorEnabled() {
        return getFlavor() == ApplicationFlavor.BETA;
    }
}
