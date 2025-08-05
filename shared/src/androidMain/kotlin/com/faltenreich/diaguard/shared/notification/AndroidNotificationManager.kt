package com.faltenreich.diaguard.shared.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import kotlin.jvm.java
import kotlin.time.Duration.Companion.seconds

class AndroidNotificationManager(private val context: Context) {

    private val systemService = context.getSystemService(Context.NOTIFICATION_SERVICE)
        as android.app.NotificationManager

    fun showNotification(
        id: Int,
        title: String,
        message: String?,
        @DrawableRes iconRes: Int,
        channelId: String,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
    ) {
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
        val intent = Intent(context, MainActivity::class.java)
        intent.setAction(Shortcut.CREATE_ENTRY.action)
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