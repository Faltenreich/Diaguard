package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.datetime.PdfDate
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfEmpty(
    date: Date,
    label: String,
    private val width: Float,
    dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    private val date = PdfDate(date, dateTimeFormatter)
    private val label = PdfCell(PdfText(label, PdfPaint.label))

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height + label.getSize().height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)

        val backgroundPosition = position.copy(y = position.y + date.getSize().height)
        val background = let {
            val size = PdfSize(
                width = page.viewport.width,
                height = label.getSize().height,
            )
            PdfBackground(size, PdfPaint.background)
        }
        background.drawOn(page, backgroundPosition)

        label.drawOn(page, backgroundPosition)
    }
}