package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfLine(
    private val start: PdfPosition,
    private val end: PdfPosition,
    private val color: Color,
    private val width: Float = .75f,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        // Unimportant
        return PdfSize.Zero
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawLine(start, end, color, width)
    }
}