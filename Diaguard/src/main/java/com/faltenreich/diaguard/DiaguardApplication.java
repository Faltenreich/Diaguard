package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Filip on 26.08.2014.
 */
public class DiaguardApplication extends Application {
    private static Context context;
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize global values
        context = getApplicationContext();
        //JodaTimeAndroid.init(this);
    }
}
