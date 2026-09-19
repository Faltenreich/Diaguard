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
        val dayPosition = position.copy(
            x = position.x + padding,
            y = position.y + padding,
        )
        day.drawOn(page, dayPosition)

        val backgroundPosition = position.copy(
            y = position.y + day.getSize(page).height + padding * 2,
        )
        val background = let {
            val size = PdfSize(
                width = page.viewport.width,
                height = label.getSize(page).height + padding * 2,
            )
            PdfBackground(size, PdfPaint.background)
        }
        background.drawOn(page, backgroundPosition)

        val labelPosition = backgroundPosition.copy(
            x = position.x + padding,
            y = backgroundPosition.y + padding,
        )
        label.drawOn(page, labelPosition)
    }
}