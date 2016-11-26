package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.data.ImportHelper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;
import com.faltenreich.diaguard.ui.view.preferences.OpenDatabaseLicense;
import com.faltenreich.diaguard.util.Helper;

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

    @Override
    public void onTerminate() {
        super.onTerminate();
        OpenFoodFactsManager.getInstance().stop();
    }

    public static Context getContext() {
        return context;
    }

    private void init() {
        context = getApplicationContext();
        JodaTimeAndroid.init(this);
        if (FoodDao.getInstance().countAll() == 0) {
            ImportHelper.importCommonFood(context, Helper.getLocale());
        }
        LicenseResolver.registerLicense(new OpenDatabaseLicense());
        PreferenceHelper.getInstance().migrateFactors();
        OpenFoodFactsManager.getInstance().start();
    }
}