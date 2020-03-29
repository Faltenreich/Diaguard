package com.faltenreich.diaguard.shared.view.theme;

import android.app.UiModeManager;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;

public enum Theme {
    LIGHT("0", AppCompatDelegate.MODE_NIGHT_NO, UiModeManager.MODE_NIGHT_NO),
    DARK("1", AppCompatDelegate.MODE_NIGHT_YES, UiModeManager.MODE_NIGHT_YES),
    TIME_BASED("2", AppCompatDelegate.MODE_NIGHT_AUTO, UiModeManager.MODE_NIGHT_AUTO),
    SYSTEM("3", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, UiModeManager.MODE_NIGHT_AUTO);

    // Workaround: Key is String due to limitations of Preferences API
    private String key;
    private int dayNightMode;
    private int uiMode;

    Theme(String key, @StyleRes int dayNightMode, int uiMode) {
        this.key = key;
        this.dayNightMode = dayNightMode;
        this.uiMode = uiMode;
    }

    public String getKey() {
        return key;
    }

    @StyleRes
    public int getDayNightMode() {
        return dayNightMode;
    }

    public int getUiMode() {
        return uiMode;
    }

    @Nullable
    public static Theme fromKey(String key) {
        for (Theme theme : Theme.values()) {
            if (theme.getKey().equals(key)) {
                return theme;
            }
        }
        return null;
    }
}