package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.datetime.Date

internal class PdfTable(
    private val date: Date,
    private val entries: List<Entry>,
    private val settings: ExportSettings,
) : PdfDrawable {

    private val text = PdfText("Placeholder", PdfPaint.normal)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val rowCount = settings.categories.size

    override fun getSize(page: PdfPage): PdfSize {
        val rowHeight = text.getSize(page).height + (padding * 2)
        return PdfSize(
            width = page.viewport.width,
            height = rowHeight * rowCount,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val rowHeight = getSize(page).height / rowCount
        settings.categories.forEachIndexed { index, category ->
            val y = position.y + (rowHeight * index)
            if (index % 2 == 0) {
                val rectangle = PdfRectangle(position.x, y, page.viewport.right, y + rowHeight)
                drawBackground(page, rectangle)
            }
            val category = PdfText(category.category.name, PdfPaint.normal)
            category.drawOn(page, PdfPosition(position.x + padding, y + padding))
        }
    }

    private fun drawBackground(page: PdfPage, rectangle: PdfRectangle) {
        val background = PdfBackground(rectangle.size, PdfPaint.background)
        background.drawOn(page, rectangle.position)
    }

    private companion object {

        const val DAY_WIDTH = 100f
        const val DAY_HOURS = 24
        const val DAY_STEP = 2
    }
}