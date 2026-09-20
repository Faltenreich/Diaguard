package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings.Category
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTable(
    date: Date,
    private val entries: List<Entry.Local>,
    private val categories: List<Category>,
    private val width: Float,
    private val dateTimeFactory: DateTimeFactory,
    dateTimeFormatter: DateTimeFormatter,
) : PdfDrawable {

    private val date = PdfDate(date, dateTimeFormatter)
    private val text = PdfText("Placeholder", PdfPaint.normal)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val rowCount = categories.size
    private val bottomSpacing = PdfSpacing.DAY_PADDING_BOTTOM.points

    override fun getSize(): PdfSize {
        val dateHeight = date.getSize().height + padding * 2
        val rowHeight = text.getSize().height + padding * 2
        return PdfSize(
            width = width,
            height = dateHeight + (rowHeight * rowCount) + bottomSpacing,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        drawDate(page, position.copy(x = position.x + padding, y = position.y + padding))
        drawHours(page, position.copy(x = position.x + DAY_WIDTH, y = position.y + padding))
        drawCategories(page, position.copy(y = position.y + date.getSize().height + padding * 2))
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
            val text = PdfText(hour.toString(), PdfPaint.normal)
            val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
            val y = position.y
            text.drawOn(page, PdfPosition(x, y))
        }
    }

    private fun drawCategories(page: PdfPage, position: PdfPosition) {
        val rowHeight = text.getSize().height + (padding * 2)
        categories.forEachIndexed { index, category ->
            val y = position.y + (rowHeight * index)
            if (index % 2 == 0) {
                val rectangle = PdfRectangle(position.x, y, page.viewport.right, y + rowHeight)
                drawBackground(page, rectangle)
            }
            val labelPosition = PdfPosition(position.x + padding, y + padding)
            val label = PdfText(category.category.name, PdfPaint.normal)
            label.drawOn(page, labelPosition)

            val hoursPosition = PdfPosition(x = labelPosition.x + DAY_WIDTH, y = labelPosition.y)
            drawCategory(page, hoursPosition, category.category)
        }
    }

    private fun drawCategory(page: PdfPage, position: PdfPosition, category: MeasurementCategory) {
        val progression = 0..<DAY_HOURS step DAY_STEP
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / progression.count()
        for (hour in progression) {
            val index = hour / progression.step
            val values = entries.flatMap { entry ->
                val entryTime = entry.dateTime.time
                val startTime = dateTimeFactory.time(hourOfDay = hour, minuteOfHour = 0)
                val endTime = dateTimeFactory.timeAtEndOf(
                    time = startTime.copy(hourOfDay = hour + DAY_STEP - 1),
                    unit = TimeUnit.HOUR,
                )
                if (entryTime in startTime..<endTime) {
                    entry.values.filter { value ->
                        value.property.category == category
                    }
                } else {
                    emptyList()
                }
            }
            if (values.isNotEmpty()) {
                val value = values.sumOf { it.value }
                val text = PdfText(value.toString(), PdfPaint.normal)
                val x =
                    position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
                val y = position.y
                text.drawOn(page, PdfPosition(x, y))
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
    }
}