package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfEmpty(
    day: String,
    label: String,
    private val viewport: PdfRectangle,
) : PdfDrawable {

    private val day = PdfDay(day)
    private val label = PdfText(label, PdfPaint.label)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val bottomSpacing = PdfSpacing.DAY_PADDING_BOTTOM.points

    override fun getSize(): PdfSize {
        return PdfSize(
            width = viewport.width,
            height = day.getSize().height + label.getSize().height + (padding * 4) + bottomSpacing,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val dayPosition = position.copy(
            x = position.x + padding,
            y = position.y + padding,
        )
        day.drawOn(page, dayPosition)

        val backgroundPosition = position.copy(
            y = position.y + day.getSize().height + padding * 2,
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