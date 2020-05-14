package com.faltenreich.diaguard.feature.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceHelper;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;

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

    public static void showNotification(Context context, @StringRes int titleResId, String message) {
        String title = context.getString(titleResId);
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(PreferenceHelper.getInstance().isSoundAllowed() ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) : null)
                .setVibrate(shouldNotificationVibrate(context) && PreferenceHelper.getInstance().isVibrationAllowed() ? new long[]{VIBRATION_DURATION_IN_MILLIS} : null);
        Intent resultIntent = new Intent(context, EntryEditActivity.class);

        // Put target activity on back stack on top of its parent to guarantee correct back navigation
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EntryEditActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Notification will dismiss when be clicked on
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
