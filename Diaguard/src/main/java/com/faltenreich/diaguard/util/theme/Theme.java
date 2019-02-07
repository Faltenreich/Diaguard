package com.faltenreich.diaguard.util.theme;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;

public enum Theme {
    LIGHT("0", AppCompatDelegate.MODE_NIGHT_NO),
    DARK("1", AppCompatDelegate.MODE_NIGHT_YES),
    TIME_BASED("2", AppCompatDelegate.MODE_NIGHT_AUTO),
    SYSTEM("3", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    // Workaround: Key is String due to limitations of Preferences API
    private String key;
    private int dayNightMode;

    Theme(String key, @StyleRes int dayNightMode) {
        this.key = key;
        this.dayNightMode = dayNightMode;
    }

    @StyleRes
    public int getDayNightMode() {
        return dayNightMode;
    }

    public String getKey() {
        return key;
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