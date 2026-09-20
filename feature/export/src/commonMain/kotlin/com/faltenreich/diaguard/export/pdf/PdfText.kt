package com.faltenreich.diaguard.export.pdf

internal class PdfText(
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