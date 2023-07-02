package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineViewState

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    state: TimelineViewState,
    config: TimelineConfig,
) = with(config) {
    val size = state.chartSize

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = state.offset.x.toInt()
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
        if (hour == 0) {
            val xOffsetInDays = xAbsolute / widthPerDay
            val date = state.initialDate.plusDays(xOffsetInDays.toInt())
            drawDate(date, x, state.offset, state, config)
        }
        drawHour(hour, x, state, config)
    }
}

private fun DrawScope.drawDate(
    date: Date,
    x: Float,
    offset: Offset,
    state: TimelineViewState,
    config: TimelineConfig,
    dateTimeFormatter: DateTimeFormatter = inject(),
) = with(config) {
    val size = state.chartSize
    drawText(
        text = dateTimeFormatter.formatDate(date),
        x = x + padding,
        y= padding + fontSize,
        size = fontSize,
        paint = fontPaint,
    )
    // Hide date indicator initially
    if (offset.x != 0f) {
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
}

private fun DrawScope.drawHour(
    hour: Int, x: Float,
    state: TimelineViewState,
    config: TimelineConfig,
) = with(config) {
    val size = state.chartSize
    drawLine(
        color = gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = size.height),
        strokeWidth = gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + padding,
        y = size.height - padding,
        size = fontSize,
        paint = fontPaint,
    )
}