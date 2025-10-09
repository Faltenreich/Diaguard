package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.Time

class FormatDateTimeUseCase(private val dateTimeFormatter: DateTimeFormatter) {

    operator fun invoke(dateTime: DateTime): String {
        return dateTimeFormatter.formatDateTime(dateTime)
    }

    operator fun invoke(date: Date): String {
        return dateTimeFormatter.formatDate(date)
    }

    operator fun invoke(time: Time): String {
        return dateTimeFormatter.formatTime(time)
    }

    operator fun invoke(dateRange: DateRange): String {
        return dateTimeFormatter.formatDateRange(dateRange)
    }

    operator fun invoke(monthOfYear: MonthOfYear, abbreviated: Boolean): String {
        return dateTimeFormatter.formatMonthOfYear(monthOfYear, abbreviated)
    }

    fun formatDayOfMonth(date: Date): String {
        return dateTimeFormatter.formatDayOfMonth(date)
    }

    fun formatDayOfWeek(date: Date, abbreviated: Boolean): String {
        return dateTimeFormatter.formatDayOfWeek(date, abbreviated)
    }
}