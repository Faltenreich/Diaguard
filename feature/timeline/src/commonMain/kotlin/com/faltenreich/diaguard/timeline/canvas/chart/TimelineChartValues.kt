package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.view.canvas.bezierBetween

@Suppress("FunctionName")
fun DrawScope.TimelineChartValues(
    state: TimelineChartState,
    config: TimelineConfig,
) = with(state) {
    if (items.isEmpty()) return@with

    val colorStops = colorStops.map { (offset, type) ->
        offset to when (type) {
            TimelineChartState.ColorStop.Type.NONE -> config.textStyle.color
            TimelineChartState.ColorStop.Type.LOW -> config.valueColorLow
            TimelineChartState.ColorStop.Type.NORMAL -> config.valueColorNormal
            TimelineChartState.ColorStop.Type.HIGH -> config.valueColorHigh
        }
    }.toTypedArray()
    val brush = Brush.verticalGradient(
        colorStops = colorStops,
        startY = state.chartRectangle.topLeft.y,
        endY = state.chartRectangle.topLeft.y + state.chartRectangle.size.height,
    )

    config.valuePath.reset()

    drawItem(items.first(), brush, config)

    items.zipWithNext { start, end ->
        connectItems(start, end, brush, config)
        drawItem(end, brush, config)
    }
}

private fun DrawScope.drawItem(
    item: TimelineChartState.Item,
    brush: Brush,
    config: TimelineConfig,
) {
    drawCircle(
        brush = brush,
        radius = config.valueDotRadius,
        center = item.position,
        style = Fill,
    )
}

private fun DrawScope.connectItems(
    start: TimelineChartState.Item,
    end: TimelineChartState.Item,
    brush: Brush,
    config: TimelineConfig,
) {
    config.valuePath.moveTo(start.position.x, start.position.y)
    config.valuePath.bezierBetween(start.position, end.position)

    drawPath(
        path = config.valuePath,
        brush = brush,
        style = config.valueStroke,
    )
}