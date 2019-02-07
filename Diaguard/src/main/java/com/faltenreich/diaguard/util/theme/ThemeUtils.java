package com.faltenreich.diaguard.util.theme;

import com.faltenreich.diaguard.data.PreferenceHelper;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    private static void applyTheme(Theme theme) {
        AppCompatDelegate.setDefaultNightMode(theme.getDayNightMode());
    }

    public static void invalidateTheme() {
        applyTheme(PreferenceHelper.getInstance().getTheme());
    }
}
