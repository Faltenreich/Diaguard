package com.faltenreich.diaguard.datetime

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.weekday_friday
import diaguard.shared.generated.resources.weekday_friday_short
import diaguard.shared.generated.resources.weekday_monday
import diaguard.shared.generated.resources.weekday_monday_short
import diaguard.shared.generated.resources.weekday_saturday
import diaguard.shared.generated.resources.weekday_saturday_short
import diaguard.shared.generated.resources.weekday_sunday
import diaguard.shared.generated.resources.weekday_thursday
import diaguard.shared.generated.resources.weekday_thursday_short
import diaguard.shared.generated.resources.weekday_tuesday
import diaguard.shared.generated.resources.weekday_tuesday_short
import diaguard.shared.generated.resources.weekday_wednesday
import diaguard.shared.generated.resources.weekday_wednesday_short
import diaguard.shared.generated.resources.weekday_weekday_sunday_short
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
    ;

    fun previous(): DayOfWeek {
        return if (this == MONDAY) SUNDAY
        else entries[ordinal - 1]
    }

    fun next(): DayOfWeek {
        return if (this == SUNDAY) MONDAY
        else entries[ordinal + 1]
    }
}