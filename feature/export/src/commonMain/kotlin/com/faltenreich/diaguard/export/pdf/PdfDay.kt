package com.faltenreich.diaguard.export.pdf

internal class PdfDay(text: String) : PdfDrawable {

    private val text = PdfText(text, PdfPaint.bold)
    private val padding = PdfSpacing.P_4.points

    override fun getSize(page: PdfPage): PdfSize {
        val size = text.getSize(page)
        return size.copy(
            width = page.viewport.width,
            height = size.height + padding * 2,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        text.drawOn(page, position.copy(x = position.x + padding, y = position.y + padding))
        // TODO: Add hours
    }
}