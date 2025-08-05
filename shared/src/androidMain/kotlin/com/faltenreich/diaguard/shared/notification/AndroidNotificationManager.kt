package com.faltenreich.diaguard.shared.notification

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.time.Duration.Companion.seconds

class AndroidNotificationManager(private val activity: Activity) {

    private val context = activity
    private val systemService = context.getSystemService(Context.NOTIFICATION_SERVICE)
        as android.app.NotificationManager

    fun showNotification(notification: AndroidNotification) = with(notification) {
        val vibrationDuration = 1.seconds

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setTicker(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(
                if (isSoundEnabled) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                else null
            )
            .setVibrate(
                if (isVibrationEnabled) longArrayOf(vibrationDuration.inWholeMilliseconds)
                else null
            )

        // Open entry form when clicked on
        val intent = Intent(context, activity::class.java)
        // TODO: App Shortcut
        //  intent.setAction(Shortcut.CREATE_ENTRY.action)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)
        builder.setContentIntent(pendingIntent)

        // Dismiss notification when clicked on
        val notification = builder.build()
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE or Notification.FLAG_AUTO_CANCEL

        systemService.notify(id, notification)
    }
}