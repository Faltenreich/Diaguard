package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.preference.reminder.ReminderPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

class GetReminderUseCase(
    private val getPreference: GetPreferenceUseCase,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<Duration?> {
        return getPreference(ReminderPreference).map { milliseconds ->
            when (milliseconds) {
                -1L -> null
                else -> {
                    val dateTime = dateTimeFactory.dateTime(millis = milliseconds)
                    val duration = dateTimeFactory.now().until(dateTime, TimeUnit.SECOND)
                    duration.takeIf { it > Duration.ZERO }
                }
            }
        }
    }
}