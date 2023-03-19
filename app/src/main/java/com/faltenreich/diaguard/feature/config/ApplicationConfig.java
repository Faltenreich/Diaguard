package com.faltenreich.diaguard.feature.config;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.BuildConfig;

public class ApplicationConfig {

    @Nullable
    private static ApplicationFlavor getFlavor() {
        return ApplicationFlavor.fromIdentifier(BuildConfig.FLAVOR);
    }

    public static boolean importDemoData() {
        ApplicationFlavor flavor = getFlavor();
        return flavor != null && flavor.importDemoData();
    }

    public static boolean isCalculatorEnabled() {
        ApplicationFlavor flavor = getFlavor();
        return flavor != null && flavor.isCalculatorEnabled();
    }

    public static boolean isCgmSupported() {
        ApplicationFlavor flavor = getFlavor();
        return flavor != null && flavor.isCgmSupported();
    }
}
