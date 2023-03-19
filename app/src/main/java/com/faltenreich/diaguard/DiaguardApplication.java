package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.feature.cgm.CgmRepository;
import com.faltenreich.diaguard.feature.notification.NotificationUtils;
import com.faltenreich.diaguard.feature.cgm.xdrip.XDripBroadcastReceiver;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.importing.Import;
import com.faltenreich.diaguard.shared.data.database.migration.Migrator;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import net.danlew.android.joda.JodaTimeAndroid;

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
        JodaTimeAndroid.init(this);
        PreferenceStore.getInstance().init(this);
        Database.getInstance().init(this);
        new Import(this).importDataIfNeeded();
        PreferenceStore.getInstance().migrate();
        Migrator.getInstance().start();
        NotificationUtils.setupNotifications(this);
        Theme theme = PreferenceStore.getInstance().getTheme();
        ThemeUtils.setDefaultNightMode(theme);
        ThemeUtils.setUiMode(this, theme);
        ImageLoader.getInstance().init(this);
        XDripBroadcastReceiver.invalidate(this);
        CgmRepository.getInstance().invalidateNotification(this);
    }
}