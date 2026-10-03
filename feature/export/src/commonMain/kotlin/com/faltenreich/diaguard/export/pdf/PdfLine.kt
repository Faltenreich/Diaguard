package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfLine(
    private val vector: PdfPosition,
    private val color: Color = Color.LightGray,
    private val width: Float = .75f,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return PdfSize(
            width = vector.x,
            height = vector.y,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawLine(
            start = position,
            end = position.copy(x = position.x + vector.x, y = position.y + vector.y),
            color = color,
            width = width,
        )
    }
}