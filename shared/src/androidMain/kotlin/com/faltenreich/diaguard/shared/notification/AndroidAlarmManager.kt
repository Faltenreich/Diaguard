package com.faltenreich.diaguard.shared.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlin.time.Duration

class AndroidAlarmManager(private val context: Context) : AlarmManager {

    private val nativeAlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager

    private fun getPendingIntent(requestCode: Int): PendingIntent? {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(context, requestCode, intent, flags)
    }

    override fun setAlarm(id: Long, delay: Duration) {
        val alarmStartInMillis = System.currentTimeMillis() + delay.inWholeMilliseconds
        val intent = getPendingIntent(requestCode = id.toInt()) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nativeAlarmManager.setAndAllowWhileIdle(
                android.app.AlarmManager.RTC_WAKEUP,
                alarmStartInMillis,
                intent,
            )
        } else {
            nativeAlarmManager.set(
                android.app.AlarmManager.RTC_WAKEUP,
                alarmStartInMillis,
                intent,
            )
        }
    }

    override fun cancelAlarm(id: Long) {
        val intent = getPendingIntent(requestCode = id.toInt()) ?: return
        nativeAlarmManager.cancel(intent)
    }
}