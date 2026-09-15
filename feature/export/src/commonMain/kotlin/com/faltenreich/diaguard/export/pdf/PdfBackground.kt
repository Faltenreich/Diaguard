package com.faltenreich.diaguard.export.pdf

internal data class PdfBackground(
    private val size: PdfSize,
    private val paint: PdfPaint,
) : PdfDrawable {

    override fun getSize(page: PdfPage): PdfSize {
        return size
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.drawRectangle(PdfRectangle(position, size), paint)
    }
}