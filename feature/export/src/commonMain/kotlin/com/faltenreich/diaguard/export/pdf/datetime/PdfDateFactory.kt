package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfDateFactory(private val dateTimeFormatter: DateTimeFormatter) {

    fun create(
        date: Date,
        width: Float,
        withHours: Boolean,
    ): PdfDrawable {
        val date = PdfCell(PdfText(dateTimeFormatter.formatDate(date), PdfPaint.bold))
        return if (withHours) {
            PdfDateWithHours(
                date = date,
                hours = PdfHours(
                    size = PdfSize(
                        width = width - DATE_WIDTH,
                        height = date.getSize().height,
                    )
                )
            )
        } else {
            date
        }
    }

    private companion object {

        const val DATE_WIDTH = 100f
    }
}