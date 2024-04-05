package com.faltenreich.diaguard.datetime

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class DayOfWeek(
  val label: StringResource,
  val abbreviation: StringResource,
) {
    MONDAY(
        label = Res.string.weekday_monday,
        abbreviation = Res.string.weekday_monday_short,
    ),
    TUESDAY(
        label = Res.string.weekday_tuesday,
        abbreviation = Res.string.weekday_tuesday_short,
    ),
    WEDNESDAY(
        label = Res.string.weekday_wednesday,
        abbreviation = Res.string.weekday_wednesday_short,
    ),
    THURSDAY(
        label = Res.string.weekday_thursday,
        abbreviation = Res.string.weekday_thursday_short,
    ),
    FRIDAY(
        label = Res.string.weekday_friday,
        abbreviation = Res.string.weekday_friday_short,
    ),
    SATURDAY(
        label = Res.string.weekday_saturday,
        abbreviation = Res.string.weekday_saturday_short,
    ),
    SUNDAY(
        label = Res.string.weekday_sunday,
        abbreviation = Res.string.weekday_weekday_sunday_short,
    ),
}