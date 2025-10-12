package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.shared.localization.Localization
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class JvmDateTimeFormatter(private val localization: Localization) : NativeDateTimeFormatter {

    override fun formatDate(date: Date): String {
        val localDate = LocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
        val locale = localization.getLocale().platformLocale
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
        return formatter.format(localDate)
    }
}