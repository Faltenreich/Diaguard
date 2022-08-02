package com.faltenreich.diaguard.shared.view.theme;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    @SuppressLint("WrongConstant")
    public static void setDefaultNightMode(Theme theme) {
        AppCompatDelegate.setDefaultNightMode(theme.getDayNightMode());
    }

    @SuppressLint("WrongConstant")
    public static void setUiMode(Context context, Theme theme) {
        if (context != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            UiModeManager uiModeManager = context.getSystemService(UiModeManager.class);
            if (uiModeManager != null) {
                uiModeManager.setNightMode(theme.getUiMode());
            }
        }
    }
}
