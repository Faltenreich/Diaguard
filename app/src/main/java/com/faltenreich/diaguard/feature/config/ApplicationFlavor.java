package com.faltenreich.diaguard.feature.config;

import androidx.annotation.Nullable;

public enum ApplicationFlavor {
    DEMO("demo"),
    BETA("beta"),
    PRODUCTION("production");

    private String identifier;

    ApplicationFlavor(String identifier) {
        this.identifier = identifier;
    }

    public boolean importDemoData() {
        return this == ApplicationFlavor.DEMO;
    }

    public boolean isCalculatorEnabled() {
        return this == ApplicationFlavor.BETA;
    }

    @Nullable
    public static ApplicationFlavor fromIdentifier(String identifier) {
        for (ApplicationFlavor flavor : ApplicationFlavor.values()) {
            if (flavor.identifier.equals(identifier)) {
                return flavor;
            }
        }
        return null;
    }
}
