package com.faltenreich.diaguard.entry.form.alarm

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import dev.icerock.moko.resources.StringResource

enum class AlarmDelay(
    val label: StringResource,
    val minutes: Int?,
) {
    NONE(
        label = MR.strings.alarm_none,
        minutes = null,
    ),
    IN_5_MINUTES(
        label = MR.strings.alarm_in_5_minutes,
        minutes = 5,
    ),
    IN_15_MINUTES(
        label = MR.strings.alarm_in_15_minutes,
        minutes = 15,
    ),
    IN_30_MINUTES(
        label = MR.strings.alarm_in_30_minutes,
        minutes = 30,
    ),
    IN_1_HOUR(
        label = MR.strings.alarm_in_1_hour,
        minutes = DateTimeConstants.MINUTES_PER_HOUR,
    ),
    IN_2_HOURS(
        label = MR.strings.alarm_in_2_hours,
        minutes = 2 * DateTimeConstants.MINUTES_PER_HOUR,
    ),
    // TODO
    CUSTOM(
        label = MR.strings.alarm_custom,
        minutes = null,
    ),
}