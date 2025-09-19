package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.write
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.notification.AlarmManager
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_alarm_start
import kotlin.time.Duration

class SetReminderUseCase(
    private val alarmManager: AlarmManager,
    private val keyValueStore: KeyValueStore,
    private val dateTimeFactory: DateTimeFactory,
    private val localization: Localization,
) {

    suspend operator fun invoke(delay: Duration) {
        val milliseconds = dateTimeFactory.now().epochMilliseconds + delay.inWholeMilliseconds

        alarmManager.setAlarm(id = ALARM_ID, delay = delay)

        keyValueStore.write(
            key = localization.getString(Res.string.preference_alarm_start),
            value = milliseconds,
        )
    }

    companion object {

        private const val ALARM_ID = 34248273L
    }
}