package com.faltenreich.diaguard.util.theme;

import com.faltenreich.diaguard.R;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

public enum Theme {
    LIGHT("0", R.style.AppTheme),
    DARK("1", R.style.AppTheme_Dark);

    // Workaround: Key is String due to limitations of Preferences API
    private String key;
    @StyleRes
    private int style;

    Theme(String key, @StyleRes int style) {
        this.key = key;
        this.style = style;
    }

    @StyleRes
    public int getStyle() {
        return style;
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