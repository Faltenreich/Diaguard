package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.Month
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_time_ago_days
import diaguard.shared.generated.resources.date_time_ago_hours
import diaguard.shared.generated.resources.date_time_ago_minutes
import diaguard.shared.generated.resources.date_time_ago_moments
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char
import kotlinx.datetime.plus

class KotlinxDateTimeFormatter(
    private val localization: Localization,
) : DateTimeFormatter {

    override fun formatTime(time: Time): String {
        val is24HourFormat = localization.is24HourFormat()
        val format = if (is24HourFormat) {
            LocalTime.Format {
                hour()
                char(':')
                minute()
            }
        } else {
            LocalTime.Format {
                amPmHour()
                char(':')
                minute()
                char(' ')
                amPmMarker("AM", "PM")
            }
        }
        return format.format(LocalTime(time.hourOfDay, time.minuteOfHour))
    }

    override fun formatTimePassed(start: DateTime, end: DateTime): String {
        val minutesPassed = start.until(end, TimeUnit.MINUTE).inWholeMinutes
        return when {
            minutesPassed < 2 -> localization.getString(
                Res.string.date_time_ago_moments,
            )
            minutesPassed < DateTimeConstants.MINUTES_PER_HOUR * 2 -> localization.getString(
                Res.string.date_time_ago_minutes,
                minutesPassed,
            )
            minutesPassed < DateTimeConstants.MINUTES_PER_DAY * 2 -> localization.getString(
                Res.string.date_time_ago_hours,
                minutesPassed / DateTimeConstants.MINUTES_PER_HOUR,
            )
            else -> localization.getString(
                Res.string.date_time_ago_days,
                minutesPassed / DateTimeConstants.MINUTES_PER_DAY,
            )
        }
    }

    override fun formatDate(date: Date): String {
        // TODO: Localize
        return LocalDate.Format {
            date(format = LocalDate.Formats.ISO)
        }.format(LocalDate(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth ))
    }

    override fun formatDateRange(dateRange: ClosedRange<Date>): String {
        return dateRange.run {
            "%s - %s".format(
                formatDate(start),
                formatDate(endInclusive),
            )
        }
    }

    override fun formatDateTime(dateTime: DateTime): String {
        return dateTime.run {
            "%s %s".format(
                formatDate(date),
                formatTime(time),
            )
        }
    }

    // TODO: Determine calendar week via official solution
    //  https://github.com/Kotlin/kotlinx-datetime/issues/129
    override fun formatWeek(date: Date): String {
        val localDate = LocalDate(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth)
        var weekNumber = 1
        var comparison = LocalDate(year = localDate.year, month = 1, day = 1)
        // Start at end of week
        val endOfWeek = localization.getStartOfWeek().previous()
        while (comparison.dayOfWeek != endOfWeek.fromDomain()) {
            comparison = comparison.plus(1 , DateTimeUnit.DAY)
        }
        // Stop when passing date
        while (comparison <= localDate) {
            comparison = comparison.plus(1, DateTimeUnit.WEEK)
            weekNumber += 1
        }
        // Handle first week of next year that starts in this year
        if (localDate.year != comparison.year) {
            weekNumber = 1
        }
        return weekNumber.toString()
    }

    override fun formatDayOfWeek(date: Date, abbreviated: Boolean): String {
        return date.dayOfWeek.run { localization.getString(if (abbreviated) abbreviation else label) }
    }

    override fun formatDayOfMonth(date: Date): String {
        return "%02d".format(date.dayOfMonth)
    }

    override fun formatMonth(month: Month, abbreviated: Boolean): String {
        return month.run { localization.getString(if (abbreviated) abbreviation else label) }
    }

    override fun formatMonthOfYear(monthOfYear: MonthOfYear, abbreviated: Boolean): String {
        return monthOfYear.run {
            "%s %04d".format(
                formatMonth(month, abbreviated = abbreviated),
                year,
            )
        }
    }

    override fun formatQuarter(date: Date): String {
        return date.quarter.toString()
    }

    override fun formatYear(date: Date): String {
        return date.year.toString()
    }
}