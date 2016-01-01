package com.faltenreich.diaguard;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext() {
        return context;
    }
}