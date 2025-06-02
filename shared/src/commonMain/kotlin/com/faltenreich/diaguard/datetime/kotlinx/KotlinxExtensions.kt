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

// TODO: Replace with official solution when ready
//  https://github.com/Kotlin/kotlinx-datetime/issues/129
val LocalDate.weekOfYear: WeekOfYear
    get() {
        var weekNumber = 1
        var comparison = LocalDate(year = year, monthNumber = 1, dayOfMonth = 1)
        // Start at end of week
        // TODO: Localize end of week
        while (comparison.dayOfWeek != KotlinxDayOfWeek.SUNDAY) {
            comparison = comparison.plus(1 , DateTimeUnit.DAY)
        }
        // Stop when passing date
        while (comparison <= this) {
            comparison = comparison.plus(1, DateTimeUnit.WEEK)
            weekNumber += 1
        }
        // Handle first week of next year that starts in this year
        if (year != comparison.year) {
            weekNumber = 1
        }
        return WeekOfYear(
            weekNumber = weekNumber,
            year = comparison.year,
        )
    }