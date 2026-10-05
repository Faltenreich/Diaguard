package com.faltenreich.diaguard.export.pdf.timeline

import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.export.pdf.PdfLine
import com.faltenreich.diaguard.export.pdf.PdfSpacing
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize
import kotlin.math.max

internal class PdfTimelineChart(
    private val width: Float,
    private val values: List<MeasurementValue>?,
) : PdfDrawable {

    private val height = HEIGHT
    private val maxValue = max(values?.maxOf { it.value.toFloat() } ?: 0f, 250f)
    private val xAxisLine = PdfLine(vector = PdfPosition(x = 0f, y = height))
    private val padding = PdfSpacing.P_16.points

    override fun getSize(): PdfSize {
        return if (values != null) PdfSize(width, height) else PdfSize.Zero
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val values = values ?: return
        drawYAxis(page, position)
        val position = position.copy(x = position.x + Y_AXIS_WIDTH)
        drawXAxis(page, position)
        drawValues(values, page, position)
    }

    private fun drawYAxis(page: PdfPage, position: PdfPosition) {
        val labelCount = Y_AXIS_LABEL_COUNT
        val labelHeight = height / labelCount
        (0..<labelCount).forEach { index ->
            val x = position.x
            val y = position.y + height - labelHeight * index

            val label = PdfText(index.toString(), PdfPaint.label)
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
        values.forEach {
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