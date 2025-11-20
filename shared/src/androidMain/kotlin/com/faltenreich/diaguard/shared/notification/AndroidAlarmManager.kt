package com.faltenreich.diaguard.shared.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.faltenreich.diaguard.logging.Logger
import kotlin.time.Duration

class AndroidAlarmManager(private val context: Context) : AlarmManager {

    private val systemService = context.getSystemService(Context.ALARM_SERVICE)
        as android.app.AlarmManager

    private fun getPendingIntent(alarmId: Long): PendingIntent? {
        val requestCode = alarmId.toInt()
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(context, requestCode, intent, flags)
    }

    override fun setAlarm(id: Long, delay: Duration) {
        val pendingIntent = getPendingIntent(id) ?: return
        val type = android.app.AlarmManager.RTC_WAKEUP
        val triggerAtMillis = System.currentTimeMillis() + delay.inWholeMilliseconds

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemService.setAndAllowWhileIdle(type, triggerAtMillis, pendingIntent)
        } else {
            systemService.set(type, triggerAtMillis, pendingIntent)
        }
        Logger.debug("Set alarm in $delay")
    }

    override fun cancelAlarm(id: Long) {
        val pendingIntent = getPendingIntent(id) ?: return
        systemService.cancel(pendingIntent)
    }
}