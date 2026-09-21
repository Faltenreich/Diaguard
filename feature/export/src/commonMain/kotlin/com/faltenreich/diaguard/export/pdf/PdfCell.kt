package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfCell(
    private val text: PdfText,
    private val padding: Float = PdfSpacing.CELL_PADDING.points,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        val size = text.getSize()
        return size.copy(
            width = size.width + padding * 2,
            height = size.height + padding * 2,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position.copy(x = position.x + padding, y = position.y + padding))
    }
}