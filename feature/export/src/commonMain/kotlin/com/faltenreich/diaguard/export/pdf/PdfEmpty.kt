package com.faltenreich.diaguard.export.pdf

internal class PdfEmpty(text: String) : PdfDrawable {

    private val text = PdfText(text, PdfPaint.normal)
    private val padding = PdfSpacing.CELL_PADDING.points

    override fun getSize(page: PdfPage): PdfSize {
        val size = text.getSize(page)
        return size.copy(
            width = size.width + padding * 2,
            height = size.height + padding * 2,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        drawBackground(page, position)
        text.drawOn(page, position.copy(x = position.x + padding, y = position.y + padding))
    }

    private fun drawBackground(page: PdfPage, position: PdfPosition) {
        val size = PdfSize(width = page.viewport.width, height = getSize(page).height)
        val background = PdfBackground(size, PdfPaint.background)
        background.drawOn(page, position)
    }
}