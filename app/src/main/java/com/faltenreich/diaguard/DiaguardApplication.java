package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.shared.data.database.ImportHelper;
import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.migration.Migrator;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.feature.preference.license.OpenDatabaseLicense;
import com.faltenreich.diaguard.feature.alarm.NotificationUtils;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import de.psdev.licensesdialog.LicenseResolver;

public class DiaguardApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public static Context getContext() {
        return context;
    }

    private void init() {
        context = getApplicationContext();
        JodaTimeAndroid.init(this);
        PreferenceHelper.getInstance().init(this);
        Database.getInstance().init(this);
        ImportHelper.validateImports(this);
        LicenseResolver.registerLicense(new OpenDatabaseLicense());
        PreferenceHelper.getInstance().migrate();
        Migrator.getInstance().start(this);
        NotificationUtils.setupNotifications(this);
        Theme theme = PreferenceHelper.getInstance().getTheme();
        ThemeUtils.setDefaultNightMode(theme);
        ThemeUtils.setUiMode(this, theme);
        ImageLoader.getInstance().init(this);
    }
}