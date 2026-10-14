package com.faltenreich.diaguard.export.pdf.timeline

import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfLine
import com.faltenreich.diaguard.export.pdf.PdfSpacing
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize
import kotlin.math.max
import kotlin.math.round

internal class PdfTimelineChart(
    private val width: Float,
    private val values: List<MeasurementValue>?,
) : PdfDrawable {

    private val height = HEIGHT
    private val xAxisLine = PdfLine(vector = PdfPosition(x = 0f, y = height))
    private val padding = PdfSpacing.P_16.points

    private val maxValue = max(values?.maxOf { it.value.toFloat() + 50f } ?: 0f, 210f)
    private val minValue = 0f

    override fun getSize(): PdfSize {
        return if (values != null) PdfSize(width, height) else PdfSize.Zero
    }

    // TODO: Calculate and format axis and values before
    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val values = values ?: return
        drawYAxis(page, position)
        val position = position.copy(x = position.x + Y_AXIS_WIDTH)
        drawXAxis(page, position)
        drawValues(values, page, position)
    }

    private fun getYForValue(value: Float): Float {
        return height - (value / maxValue) * height
    }

    private fun drawYAxis(page: PdfPage, position: PdfPosition) {
        val step = ((maxValue - minValue) / Y_AXIS_LABEL_COUNT).let { round((it + 10f) / 10) * 10 }
        var value = step
        while (value <= maxValue) {
            val x = position.x
            val y = position.y + getYForValue(value)

            // TODO: Format value via MeasurementValueMapper
            val label = PdfCell(PdfText(value.toInt().toString(), PdfPaint.label))
            val labelSize = label.getSize()
            val labelPosition = PdfPosition(
                x = x,
                y = y - labelSize.height / 2,
            )
            label.drawOn(page, labelPosition)

            val linePosition = PdfPosition(
                x = x + labelSize.width + padding,
                y = y,
            )
            val line = PdfLine(vector = PdfPosition(x = width - linePosition.x, y = 0f))
            line.drawOn(page, linePosition)

            value += step
        }
    }

    private fun drawXAxis(page: PdfPage, position: PdfPosition) {
        val labelCount = X_AXIS_LABEL_COUNT
        val axisWidth = page.viewport.right - position.x
        val labelWidth = axisWidth / labelCount
        (0..labelCount).forEach { index ->
            val offset = labelWidth * index
            xAxisLine.drawOn(page, position.copy(x = position.x + offset))
        }
    }

    private fun drawValues(values: List<MeasurementValue>, page: PdfPage, position: PdfPosition) {
        values.forEach { value ->
            val x = position.x + 50f // TODO: Calculate from time
            val y = position.y + (maxValue / value.value)
            // TODO: Draw dots
        }
    }

    private companion object {

        const val HEIGHT = 200f

        const val Y_AXIS_LABEL_COUNT = 6
        const val Y_AXIS_WIDTH = 100f

        const val X_AXIS_LABEL_COUNT = 12
    }
}