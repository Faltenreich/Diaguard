package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfDateWithHours(
    date: Date,
    private val width: Float,
    dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    private val date = PdfDate(date, dateTimeFormatter)
    private val height = this.date.getSize().height
    private val hours = PdfHours(PdfSize(width - DATE_WIDTH, height))

    override fun getSize(): PdfSize {
        return PdfSize(width, height)
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        hours.drawOn(page, position.copy(x = position.x + DATE_WIDTH))
    }

    private companion object {

        const val DATE_WIDTH = 100f
    }
}