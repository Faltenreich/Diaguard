package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.data.ImportHelper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.ui.view.preferences.OpenDatabaseLicense;
import com.faltenreich.diaguard.util.NotificationUtils;
import com.faltenreich.diaguard.util.theme.ThemeUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import de.psdev.licensesdialog.LicenseResolver;

/**
 * Created by Filip on 26.08.2014.
 */
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
        ImportHelper.validateImports(this);
        LicenseResolver.registerLicense(new OpenDatabaseLicense());
        PreferenceHelper.getInstance().migrate();
        NotificationUtils.setupNotifications(this);
        ThemeUtils.setTheme(false);
    }
}