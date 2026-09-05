package com.faltenreich.diaguard.export.pdf.print

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter

internal class PdfHeader(
    private val dateTime: DateTime,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    override fun drawOn(pdf: Pdf) {
        pdf.page.canvas.drawText(
            dateTimeFormatter.formatDate(dateTime.date),
            pdf.offset,
            pdf.paint.normal,
        )
    }
}