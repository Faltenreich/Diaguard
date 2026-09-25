package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfDate(
    date: Date,
    dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    private val text = PdfCell(PdfText(dateTimeFormatter.formatDate(date), PdfPaint.bold))

    override fun getSize(): PdfSize {
        return text.getSize()
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position)
    }
}