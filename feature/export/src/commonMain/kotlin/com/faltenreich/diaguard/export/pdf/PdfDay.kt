package com.faltenreich.diaguard.export.pdf

internal class PdfDay(text: String) : PdfDrawable {

    private val text = PdfText(text, PdfPaint.bold)

    override fun getSize(): PdfSize {
        return text.getSize()
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position)
    }
}