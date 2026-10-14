package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfPoint(
    private val color: Color,
    private val radius: Float,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return PdfSize(width = radius, height = radius)
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawCircle(position, color, radius)
    }
}