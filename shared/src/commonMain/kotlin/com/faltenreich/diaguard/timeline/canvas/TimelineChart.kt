package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineData

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    data: TimelineData.Chart,
) {
    val values = data.values
    if (values.isEmpty()) {
        return
    }
    val dateTimeBase = initialDate.atStartOfDay()
    val coordinateList = values.map { value ->
        val dateTime = value.entry.dateTime
        val widthPerDay = coordinates.chart.size.width
        val widthPerHour = widthPerDay / (config.xAxis.last / config.xAxis.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / config.xAxis.step) * widthPerMinute
        val x = coordinates.chart.topLeft.x + coordinates.scroll.x + offsetOfDateTime

        val percentage = (value.value - config.yAxis.first) / (config.yAxis.last - config.yAxis.first)
        val y = coordinates.chart.topLeft.y +
                coordinates.chart.size.height -
                (percentage.toFloat() * coordinates.chart.size.height)

        Offset(x, y)
    }

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            1 - config.yHighFraction to config.valueColorHigh,
            1 - config.yHighFraction to config.valueColorNormal,
            1 - config.yLowFraction to config.valueColorNormal,
            1 - config.yLowFraction to config.valueColorLow,
        ),
        startY = coordinates.chart.topLeft.y,
        endY = coordinates.chart.topLeft.y + coordinates.chart.size.height,
    )

    config.valuePath.reset()

    drawValue(coordinateList.first(), brush, config)

    coordinateList.zipWithNext { start, end ->
        connectValues(start, end, brush, config)
        drawValue(end, brush, config)
    }
}

private fun DrawScope.drawValue(
    position: Offset,
    brush: Brush,
    config: TimelineConfig,
) {
    drawCircle(
        brush = brush,
        radius = config.valueDotRadius,
        center = position,
        style = Fill,
    )
}

private fun DrawScope.connectValues(
    start: Offset,
    end: Offset,
    brush: Brush,
    config: TimelineConfig,
) {
    config.valuePath.moveTo(start.x, start.y)
    config.valuePath.bezierBetween(start, end)

    drawPath(
        path = config.valuePath,
        brush = brush,
        style = config.valueStroke,
    )
}