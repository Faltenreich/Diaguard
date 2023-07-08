package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import kotlin.math.floor

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
) = with(config) {
    drawRect(
        color = gridShadowColor,
        topLeft = timeOrigin,
        size = Size(
            width = timeSize.width,
            height = timeSize.height + dateSize.height,
        ),
        style = Fill,
    )

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xProgression = xStart .. xEnd step widthPerHour

    xProgression.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        // Hide date indicator initially
        if (hour == xAxis.first && offset.x != 0f) {
            drawDateIndicator(config, x)
        } else if (hour == xAxis.last / 2) {
            val xOffsetInDays = floor(xAbsolute / widthPerDay).toInt()
            val date = initialDate.plusDays(xOffsetInDays)
            drawDate(dateOrigin, dateSize, config, date, x)
        }
        drawHour(origin, size, timeOrigin, timeSize, dateOrigin, dateSize, offset, config, hour, x)
    }
}

private fun DrawScope.drawDateIndicator(
    config: TimelineConfig,
    x: Float,
) = with(config) {
    val gradientWidth = 40f
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            .0f to Color.Transparent,
            1f to gridShadowColor,
        ),
        startX = x - gradientWidth,
        endX = x,
    )
    drawRect(
        brush = gradient,
        topLeft = Offset(x = x - gradientWidth, y = 0f),
        size = Size(width = gradientWidth, height = size.height),
    )
}

private fun DrawScope.drawDate(
    dateOrigin: Offset,
    dateSize: Size,
    config: TimelineConfig,
    date: Date,
    x: Float,
) = with(config) {
    val dateAsText = dateTimeFormatter.formatDate(date)
    drawText(
        text = dateAsText,
        x = x - textMeasurer.measure(dateAsText).size.width / 2,
        y = dateOrigin.y + dateSize.height / 2 + fontSize / 2,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawHour(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
    hour: Int,
    x: Float,
) = with(config) {
    val lineEndY = when (hour) {
        0 -> origin.y + size.height
        else -> origin.y + size.height - timeSize.height - dateSize.height + padding
    }
    drawLine(
        color = gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = lineEndY),
        strokeWidth = gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + padding,
        y = timeOrigin.y + padding + fontSize,
        size = fontSize,
        paint = fontPaint,
    )
}