package com.faltenreich.diaguard.shared;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.Nullable;

public class SystemUtils {

    private static final String TAG = SystemUtils.class.getSimpleName();

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    @Nullable
    public static String getLabelForActivity(Activity activity) {
        try {
            ComponentName componentName = new ComponentName(activity, activity.getClass());
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(componentName, 0);
            return activity.getString(activityInfo.labelRes);
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }
}
