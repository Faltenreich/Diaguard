package com.faltenreich.diaguard.shared.notification

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
import kotlin.time.Duration.Companion.seconds

// FIXME: NoDefinitionFoundException: No definition found for type 'android.app.Activity'
class AndroidNotificationManager(private val activity: Activity) {

    private val context = activity
    private val systemService = context.getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

    // TODO: Refactor
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