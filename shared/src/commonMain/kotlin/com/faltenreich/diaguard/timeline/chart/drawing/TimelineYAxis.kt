package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.chart.TimelineChartConfig

@Suppress("FunctionName")
fun DrawScope.TimelineYAxis(
    config: TimelineChartConfig,
) = with(config) {
    val height = size.height / (yAxis.last / yAxis.step)
    yAxis
        .drop(1)
        .dropLast(1)
        .forEach { value ->
            val index = yAxis.indexOf(value)
            val x = 0f + padding
            val y = size.height - (index * height)
            drawLine(
                color = gridStrokeColor,
                start = Offset(x = 0f, y = y),
                end = Offset(x = size.width, y = y),
                strokeWidth = gridStrokeWidth,
            )
            drawText(
                text = value.toString(),
                x= x,
                y = y - padding,
                size = fontSize,
                paint = fontPaint,
            )
        }
}