package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Filip on 26.08.2014.
 */
public class DiaguardApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        JodaTimeAndroid.init(this);
        migrate();
        OpenFoodFactsManager.getInstance().start();
        OpenFoodFactsManager.getInstance().getProduct("737628064502");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        OpenFoodFactsManager.getInstance().stop();
    }

    public static Context getContext() {
        return context;
    }

    private void migrate() {
        PreferenceHelper.getInstance().migrateFactors();
    }
}