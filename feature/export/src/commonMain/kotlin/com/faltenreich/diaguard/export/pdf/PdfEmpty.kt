package com.faltenreich.diaguard.export.pdf

internal class PdfEmpty(
    day: String,
    label: String,
) : PdfDrawable {

    private val day = PdfDay(day)
    private val label = PdfText(label, PdfPaint.label)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val bottomSpacing = PdfSpacing.DAY_PADDING_BOTTOM.points

    override fun getSize(page: PdfPage): PdfSize {
        return PdfSize(
            width = page.viewport.width,
            height = day.getSize(page).height + label.getSize(page).height + (padding * 4) + bottomSpacing,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        day.drawOn(page, position.copy(x = position.x + padding, y = position.y + padding))

        val dayOffset = position.y + day.getSize(page).height + (padding * 2)
        drawBackground(page, position.copy(y = dayOffset))
        label.drawOn(page, position.copy(x = position.x + padding, y = dayOffset + padding))
    }

    private fun drawBackground(page: PdfPage, position: PdfPosition) {
        val size = PdfSize(
            width = page.viewport.width,
            height = label.getSize(page).height + padding * 2,
        )
        val background = PdfBackground(size, PdfPaint.background)
        background.drawOn(page, position)
    }
}