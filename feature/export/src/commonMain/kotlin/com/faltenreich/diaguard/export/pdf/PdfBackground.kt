package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

data class PdfBackground(
    private val size: PdfSize,
    private val paint: PdfPaint,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return size
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawRectangle(PdfRectangle(position, size), paint)
    }
}