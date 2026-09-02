package com.faltenreich.diaguard.export.pdf.print

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter

internal class PdfHeader(
    private val dateTime: DateTime,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfPrintable {

    override fun draw(state: PdfState) = with(state) {
        page.canvas.drawText(
            dateTimeFormatter.formatDate(dateTime.date),
            offset,
            paint.normal,
        )
    }
}