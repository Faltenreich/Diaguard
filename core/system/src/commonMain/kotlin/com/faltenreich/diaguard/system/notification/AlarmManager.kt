package com.faltenreich.diaguard.system.notification

import kotlin.time.Duration

interface AlarmManager {

    fun setAlarm(id: Long, delay: Duration)

    fun cancelAlarm(id: Long)
}