package com.faltenreich.diaguard.shared.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.faltenreich.diaguard.R
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.shared.logging.Logger

class AlarmBroadcastReceiver(
    private val notificationManager: AndroidNotificationManager = inject(),
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.debug("Received broadcast")
        context ?: return
        val notification = AndroidNotification(
            id = NOTIFICATION_ID,
            title = context.getString(R.string.notification_reminder_title),
            message = context.getString(R.string.notification_reminder_description),
            iconRes = R.mipmap.ic_notification,
            channelId = NOTIFICATION_CHANNEL_ID,
        )
        notificationManager.showNotification(notification)
    }

    companion object {

        private const val NOTIFICATION_ID = 34248273
        private const val NOTIFICATION_CHANNEL_ID = "diaguard_general"
    }
}