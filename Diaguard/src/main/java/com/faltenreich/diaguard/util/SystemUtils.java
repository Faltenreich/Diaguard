package com.faltenreich.diaguard.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.faltenreich.diaguard.DiaguardApplication;

import java.text.DecimalFormat;

/**
 * Created by Faltenreich on 26.06.2016.
 */
@SuppressWarnings("WeakerAccess")
public class SystemUtils {

    private static final String TAG = SystemUtils.class.getSimpleName();

    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 123;

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

    public static void playSound() {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(
                    DiaguardApplication.getContext().getApplicationContext(),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void vibrate(int timeInMilliseconds) {
        Vibrator vibrator = (Vibrator) DiaguardApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(timeInMilliseconds);
        }
    }

    public static boolean canWriteExternalStorage(Activity activity) {
        int filePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return filePermission == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionWriteExternalStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_WRITE_EXTERNAL_STORAGE);
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
