package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings.Category
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
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
    private val decimalPlaces: Int,
    private val dateTimeFactory: DateTimeFactory,
    dateTimeFormatter: DateTimeFormatter,
    private val valueMapper: MeasurementValueMapper,
    private val tintMapper: MeasurementValueTintMapper,
) : PdfDrawable {

    private val date = PdfDate(date, dateTimeFormatter)
    private val text = PdfText("Placeholder", PdfPaint.normal)
    private val padding = PdfSpacing.CELL_PADDING.points
    private val rowCount = categories.sumOf { it.properties.size }
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
            val text = PdfText(hour.toString(), PdfPaint.normal)
            val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
            val y = position.y
            text.drawOn(page, PdfPosition(x, y))
        }
    }

    private fun drawValues(page: PdfPage, position: PdfPosition) {
        val rowHeight = text.getSize().height + (padding * 2)
        var index = 0
        categories.forEachIndexed { categoryIndex, category ->
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
                val categoryName = property.property.category.name
                val propertyName = property.property.name

                val labelText = listOfNotNull(
                    categoryName,
                    propertyName.takeIf { it != categoryName },
                ).joinToString(" ")
                val label = PdfText(labelText, PdfPaint.normal)
                label.drawOn(page, labelPosition)

                drawValues(
                    page = page,
                    position = PdfPosition(x = labelPosition.x + DAY_WIDTH, y = labelPosition.y),
                    property = property.property,
                )
                index += 1
            }
        }
    }

    private fun drawValues(
        page: PdfPage,
        position: PdfPosition,
        property: MeasurementProperty.Local
    ) {
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
                    entry.values.filter { value -> value.property == property }
                } else {
                    emptyList()
                }
            }
            if (values.isNotEmpty()) {
                val sum = values.sumOf { it.value }
                val value = MeasurementValue.Average(
                    value = when (property.aggregationStyle) {
                        MeasurementAggregationStyle.CUMULATIVE -> sum
                        MeasurementAggregationStyle.AVERAGE -> sum / values.size
                    },
                    property = property,
                )
                val valueLocalized = valueMapper(value, decimalPlaces).value
                val tint = tintMapper(value)

                val text = PdfText(valueLocalized, PdfPaint.normal)
                val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
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