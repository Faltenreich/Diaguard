package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.export.pdf.PdfDate
import com.faltenreich.diaguard.export.pdf.PdfSpacing
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTable(
    private val data: PdfTableData,
    dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    private val date = PdfDate(data.date, dateTimeFormatter)
    private val text = PdfText("Placeholder", PdfPaint.normal)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val rowCount = data.categories.sumOf { it.properties.size }

    override fun getSize(): PdfSize {
        val dateHeight = date.getSize().height + padding * 2
        val rowHeight = text.getSize().height + padding * 2
        return PdfSize(
            width = data.width,
            height = dateHeight + (rowHeight * rowCount),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        drawDate(page, position.copy(x = position.x + padding, y = position.y + padding))
        drawHours(page, position.copy(x = position.x + DAY_WIDTH, y = position.y + padding))
        drawValues(page, position.copy(y = position.y + date.getSize().height + padding * 2))
    }

    private fun drawDate(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
    }

    private fun drawHours(page: PdfPage, position: PdfPosition) {
        val progression = 0..<DAY_HOURS step DAY_STEP
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / progression.count()
        for (hour in progression) {
            val index = hour / progression.step
            val text = PdfText(hour.toString(), PdfPaint.label)
            val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
            val y = position.y
            text.drawOn(page, PdfPosition(x, y))
        }
    }

    private fun drawValues(page: PdfPage, position: PdfPosition) {
        val rowHeight = text.getSize().height + (padding * 2)
        var index = 0
        data.categories.forEachIndexed { categoryIndex, category ->
            category.properties.forEach { property ->
                val y = position.y + (rowHeight * index)
                if (categoryIndex % 2 == 0) {
                    drawBackground(
                        page = page,
                        rectangle = PdfRectangle(
                            left = position.x,
                            top = y,
                            right = page.viewport.right,
                            bottom = y + rowHeight,
                        ),
                    )
                }
                val labelPosition = PdfPosition(
                    x = position.x + padding,
                    y = y + padding,
                )
                property.property.drawOn(page, labelPosition)

                drawValues(
                    page = page,
                    position = PdfPosition(x = labelPosition.x + DAY_WIDTH, y = labelPosition.y),
                    property = property,
                )
                index += 1
            }
        }
    }

    private fun drawValues(
        page: PdfPage,
        position: PdfPosition,
        property: PdfTableData.Category.Property,
    ) {
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / HOURS.count()
        property.values.forEach { (hour, value) ->
            if (value != null) {
                val index = hour / HOURS.step
                val x = position.x + (index * hourWidth) + hourWidth / 2 - value.getSize().width / 2
                val y = position.y
                value.drawOn(page, PdfPosition(x, y))
            }
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
        val HOURS = 0..<DAY_HOURS step DAY_STEP
    }
}