package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek as KotlinxDayOfWeek

// Workaround for 'when' is exhaustive so 'else' is redundant here.
//  https://youtrack.jetbrains.com/issue/KT-72745/KMP-false-positive-when-expression-must-be-exhaustive-with-expect-actual-sealed-classes
@Suppress("REDUNDANT_ELSE_IN_WHEN", "KotlinRedundantDiagnosticSuppress")
fun KotlinxDayOfWeek.toDomain(): DayOfWeek {
    return when (this) {
        KotlinxDayOfWeek.MONDAY -> DayOfWeek.MONDAY
        KotlinxDayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
        KotlinxDayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
        KotlinxDayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
        KotlinxDayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
        KotlinxDayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
        KotlinxDayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
        else -> error("Unknown DayOfWeek: $this")
    }
}

fun DayOfWeek.fromDomain(): KotlinxDayOfWeek {
    return when (this) {
        DayOfWeek.MONDAY -> KotlinxDayOfWeek.MONDAY
        DayOfWeek.TUESDAY -> KotlinxDayOfWeek.TUESDAY
        DayOfWeek.WEDNESDAY -> KotlinxDayOfWeek.WEDNESDAY
        DayOfWeek.THURSDAY -> KotlinxDayOfWeek.THURSDAY
        DayOfWeek.FRIDAY -> KotlinxDayOfWeek.FRIDAY
        DayOfWeek.SATURDAY -> KotlinxDayOfWeek.SATURDAY
        DayOfWeek.SUNDAY -> KotlinxDayOfWeek.SUNDAY
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