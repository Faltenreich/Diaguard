package com.faltenreich.diaguard.feature.config;

import com.faltenreich.diaguard.BuildConfig;

public class ApplicationConfig {

    public static ApplicationFlavor getFlavor() {
        return ApplicationFlavor.fromIdentifier(BuildConfig.FLAVOR);
    }
}
