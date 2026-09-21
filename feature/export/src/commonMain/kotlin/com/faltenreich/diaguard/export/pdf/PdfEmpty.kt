package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
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
    private val label = PdfText(label, PdfPaint.label)
    private val padding = PdfSpacing.CELL_PADDING.points

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height + label.getSize().height + (padding * 4),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val dayPosition = position.copy(
            x = position.x + padding,
            y = position.y + padding,
        )
        date.drawOn(page, dayPosition)

        val backgroundPosition = position.copy(
            y = position.y + date.getSize().height + padding * 2,
        )
        val background = let {
            val size = PdfSize(
                width = page.viewport.width,
                height = label.getSize().height + padding * 2,
            )
            PdfBackground(size, PdfPaint.background)
        }
        background.drawOn(page, backgroundPosition)

        val labelPosition = backgroundPosition.copy(
            x = position.x + padding,
            y = backgroundPosition.y + padding,
        )
        label.drawOn(page, labelPosition)
    }
}