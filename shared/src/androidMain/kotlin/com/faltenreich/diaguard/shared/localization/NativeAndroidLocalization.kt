package com.faltenreich.diaguard.shared.localization

import android.content.Context
import android.text.format.DateFormat
import com.faltenreich.diaguard.datetime.DayOfWeek
import java.util.Calendar

class NativeAndroidLocalization(private val context: Context) : NativeLocalization {

    @Suppress("MagicNumber")
    override fun getStartOfWeek(): DayOfWeek {
        // TODO: Pass Locale to Calendar
        val firstDayOfWeek = Calendar.getInstance().firstDayOfWeek
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

    override fun is24HourFormat(): Boolean {
        // TODO: Pass Locale instead of Context
        return DateFormat.is24HourFormat(context)
    }
}