package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import com.faltenreich.diaguard.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_alarm_start
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

class GetReminderUseCase(
    private val keyValueStore: KeyValueStore,
    private val dateTimeFactory: DateTimeFactory,
    private val localization: Localization,
) {

    operator fun invoke(): Flow<Duration?> {
        return keyValueStore.read<Long>(
            key = localization.getString(Res.string.preference_alarm_start),
        ).map { milliseconds ->
            when (milliseconds) {
                null -> null
                else -> {
                    val dateTime = dateTimeFactory.dateTime(millis = milliseconds)
                    val duration = dateTimeFactory.now().until(dateTime, TimeUnit.SECOND)
                    duration.takeIf { it > Duration.ZERO }
                }
            }
        }
    }
}