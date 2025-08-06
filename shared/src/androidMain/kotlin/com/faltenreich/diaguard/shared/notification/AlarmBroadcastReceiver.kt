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
            id = 1337,
            title = "Title",
            message = "Message",
            iconRes = R.mipmap.ic_launcher,
            channelId = "",
            isSoundEnabled = true,
            isVibrationEnabled = true,
        )
        notificationManager.showNotification(notification)
    }
}