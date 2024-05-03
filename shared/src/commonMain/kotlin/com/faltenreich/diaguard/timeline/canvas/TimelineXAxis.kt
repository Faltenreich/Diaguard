package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

private const val GRADIENT_WIDTH = 40f
private const val GRADIENT_FADEOUT = .05f

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
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
        // Hide date indicator initially
        if (hour == config.xAxis.first && coordinates.scroll.x != 0f) {
            drawDateIndicator(x, config)
        }
        drawHour(x, hour, coordinates, config)
    }
}

private fun DrawScope.drawDateIndicator(
    x: Float,
    config: TimelineConfig,
) {
    // TODO: Add vertical gradient, like in drawHour()
    val xOffset = x - GRADIENT_WIDTH / 2
    drawLine(
        brush = Brush.horizontalGradient(
            colorStops = arrayOf(
                .0f to Color.Transparent,
                1f to config.gridShadowColor,
            ),
            startX = x - GRADIENT_WIDTH,
            endX = x,
        ),
        start = Offset(x = xOffset, y = 0f),
        end = Offset(x = xOffset, y = size.height),
        strokeWidth = GRADIENT_WIDTH,
    )
}

private fun DrawScope.drawHour(
    x: Float,
    hour: Int,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
) {
    drawLine(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            GRADIENT_FADEOUT to config.gridStrokeColor,
        ),
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = coordinates.canvas.topLeft.y + coordinates.canvas.size.height),
        strokeWidth = config.gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + config.padding,
        y = coordinates.time.topLeft.y + config.padding + config.fontSize,
        size = config.fontSize,
        paint = config.fontPaint,
    )
}