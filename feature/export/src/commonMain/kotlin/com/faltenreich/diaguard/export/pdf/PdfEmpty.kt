package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfEmpty(
    private val width: Float,
    private val date: PdfDrawable,
    private val label: PdfDrawable,
) : PdfDrawable {

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