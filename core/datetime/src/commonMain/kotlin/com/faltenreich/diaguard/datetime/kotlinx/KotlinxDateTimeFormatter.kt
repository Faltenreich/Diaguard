package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.DateTimePlatformApi
import com.faltenreich.diaguard.datetime.Month
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import diaguard.core.datetime.generated.resources.Res
import diaguard.core.datetime.generated.resources.date_time_ago_days
import diaguard.core.datetime.generated.resources.date_time_ago_hours
import diaguard.core.datetime.generated.resources.date_time_ago_minutes
import diaguard.core.datetime.generated.resources.date_time_ago_moments
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

internal class KotlinxDateTimeFormatter(
    private val localization: Localization,
    private val numberFormatter: NumberFormatter,
    private val dateTimePlatformApi: DateTimePlatformApi,
) : DateTimeFormatter, DateTimePlatformApi by dateTimePlatformApi {

    override fun formatTime(time: Time): String {
        val is24HourFormat = dateTimePlatformApi.is24HourFormat()
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

    override fun formatDateRange(dateRange: ClosedRange<Date>): String {
        return "${formatDate(dateRange.start)} - ${formatDate(dateRange.endInclusive)}"
    }

    override fun formatDateTime(dateTime: DateTime): String {
        return "${formatDate(dateTime.date)} ${formatTime(dateTime.time)}"
    }

    override fun formatWeek(date: Date): String {
        return dateTimePlatformApi.weekOfYear(date).weekNumber.toString()
    }

    override fun formatDayOfWeek(date: Date, abbreviated: Boolean): String {
        return date.dayOfWeek.run { localization.getString(if (abbreviated) abbreviation else label) }
    }

    override fun formatDayOfMonth(date: Date): String {
        return numberFormatter.invoke(date.dayOfMonth, width = 2, padZeroes = true)
    }

    override fun formatMonth(month: Month, abbreviated: Boolean): String {
        return localization.getString(if (abbreviated) month.abbreviation else month.label)
    }

    override fun formatMonthOfYear(monthOfYear: MonthOfYear, abbreviated: Boolean): String {
        val month = formatMonth(monthOfYear.month, abbreviated = abbreviated)
        val year = numberFormatter(monthOfYear.year, width = 4, padZeroes = true)
        return "$month $year"
    }

    override fun formatQuarter(date: Date): String {
        return date.quarter.toString()
    }

    override fun formatYear(date: Date): String {
        return date.year.toString()
    }
}