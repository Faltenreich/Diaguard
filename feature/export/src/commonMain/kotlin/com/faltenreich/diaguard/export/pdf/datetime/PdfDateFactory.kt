package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateFormatStyle
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable

internal class PdfDateFactory(private val dateTimeFormatter: DateTimeFormatter) {

    fun create(
        date: Date,
        width: Float,
        withHours: Boolean,
    ): PdfDrawable {
        val dayOfWeek = dateTimeFormatter.formatDayOfWeek(date, abbreviated = true)
        val dateShort = dateTimeFormatter.formatDate(date, DateFormatStyle.SHORT)
        val text = "$dayOfWeek, $dateShort"
        return PdfDateHeader(
            width = width,
            date = PdfDate(text),
            hours = PdfHours(width = width - DATE_WIDTH).takeIf { withHours },
        )
    }

    private companion object {

        const val DATE_WIDTH = 100f
    }
}