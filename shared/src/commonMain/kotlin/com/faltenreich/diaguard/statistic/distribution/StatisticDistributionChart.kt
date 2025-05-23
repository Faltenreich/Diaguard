package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.statistic.StatisticState

@Composable
fun StatisticDistributionChart(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val chartSize = Size(
            width = size.height,
            height = size.height,
        )
        val chartTopLeft = Offset(
            x = center.x - chartSize.width / 2,
            y = 0f,
        )
        drawValue(
            startAngle = 0f,
            sweepAngle = 60f,
            color = Color.Blue,
            topLeft = chartTopLeft,
            size = chartSize,
        )
    }
}

private fun DrawScope.drawValue(
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    topLeft: Offset,
    size: Size,
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = topLeft,
        size = size,
    )
}