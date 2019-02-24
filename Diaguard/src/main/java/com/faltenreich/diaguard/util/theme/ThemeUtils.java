package com.faltenreich.diaguard.util.theme;

import android.app.UiModeManager;
import android.content.Context;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.data.PreferenceHelper;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    public static void setTheme(boolean force) {
        Theme theme = PreferenceHelper.getInstance().getTheme();
        setDefaultNightMode(theme);
        if (force) {
            setUiMode(DiaguardApplication.getContext(), theme);
        }
    }

    private static void setDefaultNightMode(Theme theme) {
        AppCompatDelegate.setDefaultNightMode(theme.getDayNightMode());
    }

    private static void setUiMode(Context context, Theme theme) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            UiModeManager uiModeManager = context.getSystemService(UiModeManager.class);
            uiModeManager.setNightMode(theme.getUiMode());
        }
    }
}
