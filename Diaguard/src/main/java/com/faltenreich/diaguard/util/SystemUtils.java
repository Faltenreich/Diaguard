package com.faltenreich.diaguard.util;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.activity.EntryActivity;

import java.text.DecimalFormat;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class SystemUtils {

    private static final String TAG = SystemUtils.class.getSimpleName();

    public static final int NOTIFICATION_ID = 34248273;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 123;

    public static String getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public static void showNotification(@StringRes int titleResId, String message) {
        Context context = DiaguardApplication.getContext();
        String title = context.getString(titleResId);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setTicker(title)
                        .setWhen(1000);
        Intent resultIntent = new Intent(context, EntryActivity.class);

        // Put target activity on back stack on top of its parent to guarantee correct back navigation
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EntryActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Notification will dismiss when be clicked on
        Notification notification = builder.build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
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
        vibrator.vibrate(timeInMilliseconds);
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
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
