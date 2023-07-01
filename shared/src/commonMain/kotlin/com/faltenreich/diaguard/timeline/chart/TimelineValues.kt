package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineState

@Suppress("FunctionName")
fun DrawScope.TimelineValues(
    state: TimelineState,
    config: TimelineConfig,
) = with(config) {
    val coordinates = state.values.map { value ->
        val dateTimeBase = state.initialDate.atTime(Time.atStartOfDay())
        val dateTime = value.entry.dateTime
        val widthPerDay = size.width
        val widthPerHour = widthPerDay / (xAxis.last / xAxis.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / xAxis.step) * widthPerMinute
        val x = state.offset.x + offsetOfDateTime

        val percentage = (value.value - yAxis.first) / (yAxis.last - yAxis.first)
        val y = size.height - (percentage.toFloat() * size.height)

        Offset(x, y)
    }
    if (coordinates.isEmpty()) {
        return@with
    }

    // TODO: Get percentages from extremas
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            .3f to valueColorHigh,
            .35f to valueColorNormal,
            .8f to valueColorNormal,
            .85f to valueColorLow,
        ),
    )

    val path = Path()
    path.reset()

    drawValue(coordinates.first(), valueDotRadius, brush)

    val style = Stroke(width = valueStrokeWidth)
    coordinates.zipWithNext { start, end ->
        path.moveTo(start.x, start.y)
        path.bezierBetween(start, end)

        drawPath(
            path = path,
            brush = brush,
            style = style,
        )
        drawValue(end, valueDotRadius, brush)
    }
}

private fun DrawScope.drawValue(position: Offset, dotRadius: Float, brush: Brush) {
    drawCircle(
        brush = brush,
        radius = dotRadius,
        center = position,
        style = Fill,
    )
}