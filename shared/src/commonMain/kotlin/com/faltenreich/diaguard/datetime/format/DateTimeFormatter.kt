package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.datetime.Month
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format

class DateTimeFormatter {

    fun formatDateTime(dateTime: DateTime): String {
        return dateTime.run {
            "%s %s".format(
                formatDate(date),
                formatTime(time),
            )
        }
    }

    fun formatDate(date: Date): String {
        return date.run {
            "%02d.%02d.%04d".format(
                dayOfMonth,
                monthNumber,
                year,
            )
        }
    }

    fun formatDateRange(dateRange: ClosedRange<Date>): String {
        return dateRange.run {
            "%s - %s".format(
                formatDate(start),
                formatDate(endInclusive),
            )
        }
    }

    fun formatMonth(month: Month, abbreviated: Boolean): String {
        return month.run { getString(if (abbreviated) abbreviation else label) }
    }

    fun formatMonthOfYear(monthOfYear: MonthOfYear, abbreviated: Boolean): String {
        return monthOfYear.run {
            "%s %04d".format(
                formatMonth(month, abbreviated = abbreviated),
                year,
            )
        }
    }

    fun formatDayOfMonth(date: Date): String {
        return "%02d".format(date.dayOfMonth)
    }

    fun formatDayOfWeek(date: Date, abbreviated: Boolean): String {
        return date.dayOfWeek.run { getString(if (abbreviated) abbreviation else label) }
    }

    fun formatTime(time: Time): String {
        return time.run {
            "%02d:%02d".format(
                hourOfDay,
                minuteOfHour,
            )
        }
    }

    fun formatTimePassed(start: DateTime, end: DateTime): String {
        val minutesPassed = start.minutesUntil(end)
        return when {
            minutesPassed < 2 -> getString(MR.strings.date_time_ago_moments)
            minutesPassed < DateTimeConstants.MINUTES_PER_HOUR * 2 -> getString(
                MR.strings.date_time_ago_minutes,
                minutesPassed,
            )
            minutesPassed < DateTimeConstants.MINUTES_PER_DAY * 2 -> getString(
                MR.strings.date_time_ago_hours,
                minutesPassed / DateTimeConstants.MINUTES_PER_HOUR,
            )
            else -> getString(
                MR.strings.date_time_ago_days,
                minutesPassed / DateTimeConstants.MINUTES_PER_DAY,
            )
        }
    }
}