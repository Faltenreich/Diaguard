package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.feature.alarm.NotificationUtils;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.importing.Import;
import com.faltenreich.diaguard.shared.data.database.migration.Migrator;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.image.PicassoLoader;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

public class DiaguardApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Deprecated
    public static Context getContext() {
        return context;
    }

    private void init() {
        context = getApplicationContext();
        PreferenceStore.getInstance().init(this);
        Database.getInstance().init(this);
        new Import(this).importDataIfNeeded();
        PreferenceStore.getInstance().migrate();
        Migrator.getInstance().start();
        NotificationUtils.setupNotifications(this);
        Theme theme = PreferenceStore.getInstance().getTheme();
        ThemeUtils.setDefaultNightMode(theme);
        ThemeUtils.setUiMode(this, theme);
        ImageLoader.getInstance().init(new PicassoLoader(this));
    }
}