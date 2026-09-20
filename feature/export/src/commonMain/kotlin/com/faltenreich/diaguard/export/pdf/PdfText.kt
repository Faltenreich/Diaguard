package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

class PdfText(
    private val text: String,
    private val paint: PdfPaint,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return paint.getTextBounds(text)
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawText(
            text = text,
            position = position,
            paint = paint,
        )
    }
}