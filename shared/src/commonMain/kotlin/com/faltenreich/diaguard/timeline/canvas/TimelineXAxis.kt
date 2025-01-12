@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    drawRect(
        color = config.gridShadowColor,
        topLeft = coordinates.time.topLeft,
        size = Size(
            width = coordinates.time.size.width,
            height = coordinates.time.size.height,
        ),
        style = Fill,
    )

    val widthPerDay = coordinates.canvas.size.width
    val widthPerHour = (widthPerDay / config.xAxisLabelCount).toInt()

    val xOffset = coordinates.scroll.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (config.xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xOfHours = xStart .. xEnd step widthPerHour

    xOfHours.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % config.xAxis.last) * config.xAxis.step) % config.xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + config.xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        if (hour == config.xAxis.first) {
            drawDateIndicator(x, coordinates, config)
        }
        drawHour(x, hour, coordinates, config, textMeasurer)
    }
}

private fun DrawScope.drawDateIndicator(
    x: Float,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
) {
    drawLine(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            .025f to config.gridStrokeColor,
        ),
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = coordinates.canvas.bottom - coordinates.time.height),
        strokeWidth = config.gridStrokeWidth * 2,
    )
}

private fun DrawScope.drawHour(
    x: Float,
    hour: Int,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val text = hour.toString()
    val textSize = textMeasurer.measure(text)

    drawLine(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            .025f to config.gridStrokeColor,
        ),
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = coordinates.canvas.bottom - coordinates.time.height),
        strokeWidth = config.gridStrokeWidth,
    )

    drawText(
        text = hour.toString(),
        x = x - (textSize.size.width / 2),
        y = coordinates.time.top + coordinates.time.height / 2 + (textSize.size.height / 2) - (config.padding / 3),
        size = config.fontSize,
        paint = config.fontPaint,
    )
}