package com.faltenreich.diaguard.shared.notification

import com.faltenreich.diaguard.system.notification.AlarmManager
import kotlin.time.Duration

class FakeAlarmManager : AlarmManager {

    override fun setAlarm(id: Long, delay: Duration) = Unit

    override fun cancelAlarm(id: Long) = Unit
}