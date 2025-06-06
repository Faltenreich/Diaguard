package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineData

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    data: TimelineData.Chart,
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
) {
    val values = data.values
    if (values.isEmpty()) {
        return
    }
    val dateTimeBase = initialDate.atStartOfDay()
    val coordinateList = values.map { value ->
        val dateTime = value.dateTime

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

    val colorStops = mutableListOf<Pair<Float, Color>>()
    config.yHigh?.let {
        val yHighFraction = (config.yHigh - config.yMin).toFloat() / (config.yMax - config.yMin).toFloat()
        colorStops.add(1 - yHighFraction to config.valueColorHigh)
        colorStops.add(1 - yHighFraction to config.valueColorNormal)
    }
    config.yLow?.let {
        val yLowFraction: Float = (config.yLow - config.yMin).toFloat() / (config.yMax - config.yMin).toFloat()
        colorStops.add(1 - yLowFraction to config.valueColorNormal)
        colorStops.add(1 - yLowFraction to config.valueColorLow)
    }
    if (colorStops.isEmpty()) {
        colorStops.add(0f to config.valueColorNormal)
        colorStops.add(1f to config.valueColorNormal)
    }
    val brush = Brush.verticalGradient(
        colorStops = colorStops.toTypedArray(),
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