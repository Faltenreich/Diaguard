package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.data.preference.reminder.ReminderPreference
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.preference.SetPreferenceUseCase
import com.faltenreich.diaguard.system.notification.AlarmManager
import kotlin.time.Duration

class SetReminderUseCase(
    private val alarmManager: AlarmManager,
    private val setPreference: SetPreferenceUseCase,
    private val dateTimeFactory: DateTimeFactory,
) {

    suspend operator fun invoke(delay: Duration?) {
        when (delay) {
            null -> {
                alarmManager.cancelAlarm(ALARM_ID)
                setPreference(ReminderPreference, -1L)
            }
            else -> {
                val milliseconds = dateTimeFactory.now().epochMilliseconds + delay.inWholeMilliseconds

                alarmManager.setAlarm(id = ALARM_ID, delay = delay)

                setPreference(ReminderPreference, milliseconds)
            }
        }
    }

    companion object {

        private const val ALARM_ID = 34248273L
    }
}