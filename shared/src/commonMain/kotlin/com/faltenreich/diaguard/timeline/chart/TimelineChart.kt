package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    values: List<MeasurementValue>,
) {
    TimelineChart(
        origin = coordinates.chart.topLeft,
        size = coordinates.chart.size,
        offset = coordinates.scroll,
        config = config,
        values = values,
    )
}

@Suppress("FunctionName")
private fun DrawScope.TimelineChart(
    origin: Offset,
    size: Size,
    offset: Offset,
    config: TimelineConfig,
    values: List<MeasurementValue>,
) = with(config) {
    if (values.isEmpty()) {
        return@with
    }
    val dateTimeBase = initialDate.atStartOfDay()
    val coordinates = values.map { value ->
        val dateTime = value.entry.dateTime
        val widthPerDay = size.width
        val widthPerHour = widthPerDay / (xAxis.last / xAxis.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / xAxis.step) * widthPerMinute
        val x = origin.x + offset.x + offsetOfDateTime

        val percentage = (value.value - yAxis.first) / (yAxis.last - yAxis.first)
        val y = origin.y + size.height - (percentage.toFloat() * size.height)

        Offset(x, y)
    }

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            1 - yHighFraction to valueColorHigh,
            1 - yHighFraction to valueColorNormal,
            1 - yLowFraction to valueColorNormal,
            1 - yLowFraction to valueColorLow,
        ),
        startY = origin.y,
        endY = origin.y + size.height,
    )

    valuePath.reset()

    drawValue(coordinates.first(), brush, config)

    coordinates.zipWithNext { start, end ->
        connectValues(start, end, brush, config)
        drawValue(end, brush, config)
    }
}

private fun DrawScope.drawValue(
    position: Offset,
    brush: Brush,
    config: TimelineConfig,
) = with(config) {
    drawCircle(
        brush = brush,
        radius = valueDotRadius,
        center = position,
        style = Fill,
    )
}

private fun DrawScope.connectValues(
    start: Offset,
    end: Offset,
    brush: Brush,
    config: TimelineConfig,
) = with(config) {
    valuePath.moveTo(start.x, start.y)
    valuePath.bezierBetween(start, end)
    drawPath(
        path = valuePath,
        brush = brush,
        style = valueStroke,
    )
}