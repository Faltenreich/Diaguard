package com.faltenreich.diaguard.feature.shortcut;

import androidx.annotation.Nullable;

public enum Shortcut {

    CREATE_ENTRY("com.faltenreich.diaguard.NEW_ENTRY");

    public String action;

    Shortcut(String action) {
        this.action = action;
    }

    @Nullable
    static Shortcut forAction(String action) {
        for (Shortcut shortcut : Shortcut.values()) {
            if (action.equals(shortcut.action)) {
                return shortcut;
            }
        }
        return null;
    }
}
