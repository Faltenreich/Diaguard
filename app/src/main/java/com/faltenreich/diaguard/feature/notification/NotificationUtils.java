package com.faltenreich.diaguard.feature.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.shortcut.Shortcut;

import org.joda.time.DateTimeConstants;

/**
 * Created by Faltenreich on 20.09.2017
 */

public class NotificationUtils {

    private static final int ALARM_NOTIFICATION_ID = 34248273;
    private static final int CGM_NOTIFICATION_ID = 34248274;
    private static final int VIBRATION_DURATION_IN_MILLIS = DateTimeConstants.MILLIS_PER_SECOND;

    private static NotificationManagerCompat getNotificationManager(Context context) {
        return NotificationManagerCompat.from(context);
    }

    public static void setupNotifications(Context context) {
        for (NotificationChannel notificationChannel : NotificationChannel.values()) {
            if (notificationChannel.isActive()) {
                NotificationChannelCompat notificationChannelCompat = new NotificationChannelCompat.Builder(
                    notificationChannel.getId(),
                    notificationChannel.getImportance())
                    .setName(context.getString(notificationChannel.getLabelRes()))
                    .setVibrationEnabled(!notificationChannel.isOngoing())
                    .setShowBadge(!notificationChannel.isOngoing())
                    .build();
                getNotificationManager(context).createNotificationChannel(notificationChannelCompat);
            }
        }
    }

    public static void showNotification(Context context, @StringRes int titleResId, String message) {
        NotificationChannel notificationChannel = NotificationChannel.ALARM;
        String title = context.getString(titleResId);
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, notificationChannel.getId())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(PreferenceStore.getInstance().isSoundAllowed()
                    ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) 
                    : null)
                .setVibrate(!notificationChannel.isOngoing() && PreferenceStore.getInstance().isVibrationAllowed()
                    ? new long[]{VIBRATION_DURATION_IN_MILLIS}
                    : null);

        // Open entry form when clicked on
        Intent intent  = new Intent(context, MainActivity.class);
        intent.setAction(Shortcut.CREATE_ENTRY.action);
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent , flags);
        builder.setContentIntent(pendingIntent);

        // Dismiss notification when clicked on
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;

        getNotificationManager(context).notify(ALARM_NOTIFICATION_ID, notification);
    }

    public static void updateOngoingNotification(Context context, String title, Bitmap icon) {
        NotificationChannel notificationChannel = NotificationChannel.CGM;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel.getId())
            .setContentTitle(title)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(IconCompat.createWithBitmap(icon));
        } else {
            builder.setSmallIcon(R.drawable.ic_notification);
        }
        Notification notification = builder.build();
        getNotificationManager(context).notify(CGM_NOTIFICATION_ID, notification);
    }

    public static void hideOngoingNotification(Context context) {
        getNotificationManager(context).cancel(CGM_NOTIFICATION_ID);
    }
}
