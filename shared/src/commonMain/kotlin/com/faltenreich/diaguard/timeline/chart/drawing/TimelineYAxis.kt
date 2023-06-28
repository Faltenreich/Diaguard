package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.chart.TimelineChartConfig
import com.faltenreich.diaguard.timeline.chart.TimelineChartState

class TimelineYAxis(
    private val config: TimelineChartConfig,
) {

    fun drawOn(drawScope: DrawScope, state: TimelineChartState) {
        drawScope.drawAxis()
    }

    private fun DrawScope.drawAxis() = with(config) {
        val height = size.height / (yAxis.last / yAxis.step)
        // TODO: Move window with offset
        yAxis.drop(1).dropLast(1).forEach { value ->
            val index = yAxis.indexOf(value)
            val x = 0f + padding
            val y = size.height - (index * height)
            drawText(value.toString(), x, y, fontSize, fontPaint)
            drawLine(Color.LightGray, start = Offset(x = 0f, y = y), end = Offset(x = size.width, y = y))
        }
    }
}