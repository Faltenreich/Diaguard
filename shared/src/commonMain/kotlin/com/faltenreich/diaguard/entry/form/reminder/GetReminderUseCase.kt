package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_alarm_start
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

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
                    val minutesUntil = dateTimeFactory.now().minutesUntil(dateTime)
                    // FIXME: Ignores mere seconds left
                    if (minutesUntil >= 0) minutesUntil.minutes else null
                }
            }
        }
    }
}