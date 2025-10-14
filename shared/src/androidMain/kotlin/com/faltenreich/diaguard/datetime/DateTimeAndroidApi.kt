package com.faltenreich.diaguard.datetime

import android.content.Context
import android.text.format.DateFormat
import com.faltenreich.diaguard.shared.localization.Localization
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

class DateTimeAndroidApi(
    private val localization: Localization,
    private val context: Context,
) : DateTimePlatformApi {

    override fun formatDate(date: Date): String {
        val localDate = LocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
        val locale = localization.getLocale().platformLocale
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
        return formatter.format(localDate)
    }

    @Suppress("MagicNumber")
    override fun getStartOfWeek(): DayOfWeek {
        val locale = localization.getLocale().platformLocale
        val firstDayOfWeek = Calendar.getInstance(locale).firstDayOfWeek
        return when (firstDayOfWeek) {
            1 -> DayOfWeek.SUNDAY
            2 -> DayOfWeek.MONDAY
            3 -> DayOfWeek.TUESDAY
            4 -> DayOfWeek.WEDNESDAY
            5 -> DayOfWeek.THURSDAY
            6 -> DayOfWeek.FRIDAY
            7 -> DayOfWeek.SATURDAY
            else -> DayOfWeek.MONDAY
        }
    }

    override fun weekOfYear(date: Date): WeekOfYear {
        val locale = localization.getLocale().platformLocale
        val calendar = Calendar.getInstance(locale).apply {
            set(date.year, date.monthNumber - 1, date.dayOfMonth)
        }
        val endOfWeek = Calendar.getInstance(locale).apply {
            set(date.year, date.monthNumber - 1, date.dayOfMonth)
        }
        val lastDayOfWeek = calendar.firstDayOfWeek.let { firstDayOfWeek ->
            if (firstDayOfWeek == 1) DateTimeConstants.DAYS_PER_WEEK
            else firstDayOfWeek - 1
        }
        while (endOfWeek.get(Calendar.DAY_OF_WEEK) != lastDayOfWeek) {
            endOfWeek.add(Calendar.DATE, 1)
        }
        return WeekOfYear(
            weekNumber = calendar.get(Calendar.WEEK_OF_YEAR),
            year = endOfWeek.get(Calendar.YEAR),
        )
    }

    override fun is24HourFormat(): Boolean {
        // TODO: Pass Locale instead of Context
        return DateFormat.is24HourFormat(context)
    }
}