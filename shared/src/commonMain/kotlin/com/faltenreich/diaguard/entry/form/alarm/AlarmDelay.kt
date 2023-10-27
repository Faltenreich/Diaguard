package com.faltenreich.diaguard.entry.form.alarm

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import dev.icerock.moko.resources.StringResource

sealed class AlarmDelay(
    val minutes: Int?,
    val label: StringResource,
) {

    data object None : AlarmDelay(
        minutes = null,
        label = MR.strings.alarm_none,
    )

    data object In5Minutes : AlarmDelay(
        minutes = 5,
        label = MR.strings.alarm_in_5_minutes,
    )

    data object In15Minutes : AlarmDelay(
        minutes = 15,
        label = MR.strings.alarm_in_15_minutes,
    )

    data object In30Minutes : AlarmDelay(
        minutes = 30,
        label = MR.strings.alarm_in_30_minutes,
    )

    data object In1Hour : AlarmDelay(
        minutes = DateTimeConstants.MINUTES_PER_HOUR,
        label = MR.strings.alarm_in_1_hour,
    )

    data object In2Hours : AlarmDelay(
        minutes = 2 * DateTimeConstants.MINUTES_PER_HOUR,
        label = MR.strings.alarm_in_2_hours,
    )

    class Custom(minutes: Int?) : AlarmDelay(
        minutes = minutes,
        label = MR.strings.alarm_custom,
    )

    companion object {

        val entries: List<AlarmDelay>
            get() = listOf(
                None,
                In5Minutes,
                In15Minutes,
                In30Minutes,
                In1Hour,
                In2Hours,
                Custom(minutes = null),
            )
    }
}