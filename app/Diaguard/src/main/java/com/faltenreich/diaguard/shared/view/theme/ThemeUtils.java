package com.faltenreich.diaguard.shared.view.theme;

import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    public static void setDefaultNightMode(Theme theme) {
        AppCompatDelegate.setDefaultNightMode(theme.getDayNightMode());
    }

    public static void setUiMode(Context context, Theme theme) {
        if (context != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            UiModeManager uiModeManager = context.getSystemService(UiModeManager.class);
            if (uiModeManager != null) {
                uiModeManager.setNightMode(theme.getUiMode());
            }
        }
    }
}
