package com.faltenreich.diaguard.util.theme;

import android.app.UiModeManager;
import android.content.Context;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.data.PreferenceHelper;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    private static void applyTheme(Theme theme, boolean commit) {
        AppCompatDelegate.setDefaultNightMode(theme.getDayNightMode());
        if (commit && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            UiModeManager uiModeManager = DiaguardApplication.getContext().getSystemService(UiModeManager.class);
            uiModeManager.setNightMode(theme.getUiMode());
        }
    }

    public static void invalidateTheme(boolean commit) {
        applyTheme(PreferenceHelper.getInstance().getTheme(), commit);
    }
}
