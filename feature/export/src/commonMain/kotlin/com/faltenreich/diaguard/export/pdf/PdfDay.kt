package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.export.pdf.core.PdfDrawable
import com.faltenreich.diaguard.export.pdf.core.PdfPage
import com.faltenreich.diaguard.export.pdf.core.PdfPaint
import com.faltenreich.diaguard.export.pdf.core.PdfPosition
import com.faltenreich.diaguard.export.pdf.core.PdfSize
import com.faltenreich.diaguard.export.pdf.core.PdfText

internal class PdfDay(text: String) : PdfDrawable {

    private val text = PdfText(text, PdfPaint.bold)

    override fun getSize(): PdfSize {
        return text.getSize()
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position)
    }
}