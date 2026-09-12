package com.faltenreich.diaguard.export.pdf

internal class PdfText(
    private val text: String,
    private val paint: PdfPaint,
) : PdfDrawable {

    override fun getSize(page: PdfPage): PdfSize {
        return paint.getTextBounds(text)
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawText(text, position, paint)
    }
}