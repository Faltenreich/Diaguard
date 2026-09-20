package com.faltenreich.diaguard.export.pdf.core

internal data class PdfBackground(
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