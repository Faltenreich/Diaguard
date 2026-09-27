package com.faltenreich.diaguard.export.pdf.log

import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfLog(
    private val width: Float,
    private val date: PdfDrawable,
    private val rows: List<Row>,
) : PdfDrawable {

    data class Row(
        val time: PdfDrawable,
        val items: List<Item>,
    ) {

        data class Item(
            val label: PdfDrawable,
            val content: PdfDrawable,
        )
    }

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height +
                rows.sumOf { it.items.sumOf { it.content.getSize().height.toDouble() } }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)

        var position = position.copy(y = position.y + date.getSize().height)
        rows.forEachIndexed { index, row ->
            if (index % 2 == 0) {
                val height = row.items.sumOf { it.content.getSize().height.toDouble() }.toFloat()
                val rectangle = PdfRectangle(position, PdfSize(width, height))
                val background = PdfBackground(rectangle.size, PdfPaint.background)
                background.drawOn(page, rectangle.position)
            }

            row.time.drawOn(page, position)

            row.items.forEach { item ->
                item.label.drawOn(page, position.copy(x = position.x + TIME_WIDTH))
                item.content.drawOn(
                    page,
                    position.copy(x = position.x + TIME_WIDTH + LABEL_WIDTH)
                )
                position = position.copy(y = position.y + item.content.getSize().height)
            }
        }
    }

    private companion object {

        const val TIME_WIDTH = 72f
        const val LABEL_WIDTH = 100f
    }
}