package com.faltenreich.diaguard.shared.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.faltenreich.diaguard.R
import com.faltenreich.diaguard.shared.di.inject

class AlarmBroadcastReceiver(
    private val notificationManager: AndroidNotificationManager = inject(),
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = AndroidNotification(
            id = NOTIFICATION_ID,
            title = "Title",
            message = "Message",
            iconRes = R.mipmap.ic_launcher,
            channelId = NOTIFICATION_CHANNEL_ID,
            isSoundEnabled = true,
            isVibrationEnabled = true,
        )
        notificationManager.showNotification(notification)
    }

    companion object {

        private const val NOTIFICATION_ID = 34248273
        private const val NOTIFICATION_CHANNEL_ID = "diaguard_general"
    }
}