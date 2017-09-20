package com.faltenreich.diaguard.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.ui.activity.EntryActivity;

import org.joda.time.DateTimeConstants;

/**
 * Created by Faltenreich on 20.09.2017
 */

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 34248273;
    private static final String NOTIFICATION_CHANNEL_ID = "diaguard_general";
    private static final int VIBRATION_DURATION_IN_MILLIS = DateTimeConstants.MILLIS_PER_SECOND;

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private static boolean shouldNotificationVibrate(Context context) {
        return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O || getNotificationManager(context).getNotificationChannel(NOTIFICATION_CHANNEL_ID).shouldVibrate();
    }

    public static void setupNotifications(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getNotificationManager(context);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.general), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    static void showNotification(@StringRes int titleResId, String message) {
        Context context = DiaguardApplication.getContext();
        String title = context.getString(titleResId);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setTicker(title)
                        .setChannelId(NOTIFICATION_CHANNEL_ID);
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

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_ID, notification);

        if (PreferenceHelper.getInstance().isSoundAllowed()) {
            SystemUtils.playSound();
        }

        if (shouldNotificationVibrate(context) && PreferenceHelper.getInstance().isVibrationAllowed()) {
            SystemUtils.vibrate(VIBRATION_DURATION_IN_MILLIS);
        }
    }
}
