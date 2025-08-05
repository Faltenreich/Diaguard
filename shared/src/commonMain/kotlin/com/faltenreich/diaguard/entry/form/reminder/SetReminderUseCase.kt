package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.shared.notification.AlarmManager
import kotlin.time.Duration

class SetReminderUseCase(
    private val alarmManager: AlarmManager,
) {

    operator fun invoke(delay: Duration) {
        alarmManager.setAlarm(id = ALARM_ID, delay = delay)
    }

    companion object {

        private const val ALARM_ID = 34248273L
    }
}