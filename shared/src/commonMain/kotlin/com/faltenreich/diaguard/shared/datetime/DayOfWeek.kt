package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class DayOfWeek(
  val label: StringResource,
  val abbreviation: StringResource,
) {
    MONDAY(
        label = MR.strings.weekday_monday,
        abbreviation = MR.strings.weekday_monday_short,
    ),
    TUESDAY(
        label = MR.strings.weekday_tuesday,
        abbreviation = MR.strings.weekday_tuesday_short,
    ),
    WEDNESDAY(
        label = MR.strings.weekday_wednesday,
        abbreviation = MR.strings.weekday_wednesday_short,
    ),
    THURSDAY(
        label = MR.strings.weekday_thursday,
        abbreviation = MR.strings.weekday_thursday_short,
    ),
    FRIDAY(
        label = MR.strings.weekday_friday,
        abbreviation = MR.strings.weekday_friday_short,
    ),
    SATURDAY(
        label = MR.strings.weekday_saturday,
        abbreviation = MR.strings.weekday_saturday_short,
    ),
    SUNDAY(
        label = MR.strings.weekday_sunday,
        abbreviation = MR.strings.weekday_weekday_sunday_short,
    ),
}