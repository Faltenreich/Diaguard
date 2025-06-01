package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.WeekOfYear
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.DayOfWeek as KotlinxDayOfWeek

fun KotlinxDayOfWeek.toDomain(): DayOfWeek {
    return when (this) {
        KotlinxDayOfWeek.MONDAY -> DayOfWeek.MONDAY
        KotlinxDayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
        KotlinxDayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
        KotlinxDayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
        KotlinxDayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
        KotlinxDayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
        KotlinxDayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
        else -> throw IllegalArgumentException("Unknown DayOfWeek: $this")
    }
}

fun DateUnit.fromDomain(): DateTimeUnit.DateBased {
    return when (this) {
        DateUnit.DAY -> DateTimeUnit.DAY
        DateUnit.WEEK -> DateTimeUnit.WEEK
        DateUnit.MONTH -> DateTimeUnit.MONTH
        DateUnit.QUARTER -> DateTimeUnit.QUARTER
        DateUnit.YEAR -> DateTimeUnit.YEAR
        DateUnit.CENTURY -> DateTimeUnit.CENTURY
    }
}

// FIXME: Respect (localized) start and end of week
// TODO: Replace with official solution when ready
//  https://github.com/Kotlin/kotlinx-datetime/issues/129
val LocalDate.weekOfYear: WeekOfYear
    get() {
        var weekNumber = 0
        var comparison = LocalDate(year = year, monthNumber = 1, dayOfMonth = 1)
        while (comparison <= this) {
            comparison = comparison.plus(1, DateTimeUnit.WEEK)
            weekNumber += 1
        }
        // End of year starts in first week of next year
        if (year != comparison.year) {
            weekNumber = 1
        }
        return WeekOfYear(
            weekNumber = weekNumber,
            year = comparison.year,
        )
    }