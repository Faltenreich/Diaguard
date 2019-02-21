package com.faltenreich.diaguard.util.theme;

import android.app.UiModeManager;
import android.content.Context;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.data.PreferenceHelper;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    public static void invalidateTheme(boolean commit) {
        applyTheme(PreferenceHelper.getInstance().getTheme(), commit);
    }

    private static void applyTheme(Theme theme, boolean commit) {
        setDefaultNightMode(theme);
        if (commit) {
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
