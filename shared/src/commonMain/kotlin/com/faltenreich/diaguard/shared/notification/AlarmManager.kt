package com.faltenreich.diaguard.shared.notification

import kotlin.time.Duration

interface AlarmManager {

    fun setAlarm(id: Long, delay: Duration)

    fun cancelAlarm(id: Long)
}