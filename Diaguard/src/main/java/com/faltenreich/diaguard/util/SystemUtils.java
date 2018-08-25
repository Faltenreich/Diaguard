package com.faltenreich.diaguard.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

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

    public static String getDecimalSeparator() {
        return String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity != null ? activity.getCurrentFocus() : null;
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
