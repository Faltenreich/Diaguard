package com.faltenreich.diaguard.system.notification

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.faltenreich.diaguard.R
import com.faltenreich.diaguard.logging.Logger
import kotlin.time.Duration.Companion.seconds

class AndroidNotificationManager(
    private val context: Context,
    private val activity: Activity,
) {

    private val systemService = context.getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

    fun showNotification(notification: AndroidNotification) = with(notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notification.channelId,
                context.getString(R.string.notification_channel_general),
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationChannel.enableVibration(true)
            systemService.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setTicker(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1.seconds.inWholeMilliseconds))

        // Open entry form when clicked on
        val intent = Intent(context, activity::class.java).apply {
            action = Shortcut.CREATE_ENTRY.action
        }
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
        Logger.debug("Notified system: $notification")
    }
}