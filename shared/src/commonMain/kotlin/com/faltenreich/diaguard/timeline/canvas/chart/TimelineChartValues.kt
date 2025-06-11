package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.canvas.TimelineCoordinates

@Suppress("FunctionName")
fun DrawScope.TimelineChartValues(
    state: TimelineChartState,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
) = with(state) {
    if (values.isEmpty()) return@with

    val colorStops = colorStops.map { (offset, type) ->
        offset to when (type) {
            TimelineChartState.ColorStop.Type.LOW -> config.valueColorLow
            TimelineChartState.ColorStop.Type.NORMAL -> config.valueColorNormal
            TimelineChartState.ColorStop.Type.HIGH -> config.valueColorHigh
        }
    }
    val brush = Brush.verticalGradient(
        colorStops = colorStops.toTypedArray(),
        startY = coordinates.chart.topLeft.y,
        endY = coordinates.chart.topLeft.y + coordinates.chart.size.height,
    )

    config.valuePath.reset()

    drawValue(values.first(), brush, config)

    values.zipWithNext { start, end ->
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