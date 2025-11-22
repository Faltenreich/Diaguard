package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.TimeUnit
import kotlinx.datetime.DateTimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.datetime.DayOfWeek as KotlinxDayOfWeek

// Workaround for 'when' is exhaustive so 'else' is redundant here.
//  https://youtrack.jetbrains.com/issue/KT-72745/KMP-false-positive-when-expression-must-be-exhaustive-with-expect-actual-sealed-classes
@Suppress("REDUNDANT_ELSE_IN_WHEN", "KotlinRedundantDiagnosticSuppress")
internal fun KotlinxDayOfWeek.toDomain(): DayOfWeek {
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

internal fun DayOfWeek.fromDomain(): KotlinxDayOfWeek {
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

internal fun DateUnit.fromDomain(): DateTimeUnit.DateBased {
    return when (this) {
        DateUnit.DAY -> DateTimeUnit.DAY
        DateUnit.WEEK -> DateTimeUnit.WEEK
        DateUnit.MONTH -> DateTimeUnit.MONTH
        DateUnit.QUARTER -> DateTimeUnit.QUARTER
        DateUnit.YEAR -> DateTimeUnit.YEAR
        DateUnit.CENTURY -> DateTimeUnit.CENTURY
    }
}

internal fun TimeUnit.fromDomain(): DateTimeUnit.TimeBased {
    return when (this) {
        TimeUnit.NANOSECOND -> DateTimeUnit.NANOSECOND
        TimeUnit.MILLISECOND -> DateTimeUnit.MILLISECOND
        TimeUnit.SECOND -> DateTimeUnit.SECOND
        TimeUnit.MINUTE -> DateTimeUnit.MINUTE
        TimeUnit.HOUR -> DateTimeUnit.HOUR
    }
}

internal fun Long.toDuration(unit: TimeUnit): Duration {
    return when (unit) {
        TimeUnit.NANOSECOND -> nanoseconds
        TimeUnit.MILLISECOND -> milliseconds
        TimeUnit.SECOND -> seconds
        TimeUnit.MINUTE -> minutes
        TimeUnit.HOUR -> hours
    }
}