package com.faltenreich.diaguard.export.pdf

internal class PdfDay(text: String) : PdfDrawable {

    private val text = PdfText(text, PdfPaint.bold)

    override fun getSize(page: PdfPage): PdfSize {
        return text.getSize(page)
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position)
    }
}