package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.statistic.StatisticState

@Composable
fun StatisticDistributionChart(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val chartSize = Size(width = size.height, height = size.height)
        drawArc(
            color = Color.Blue,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(
                x = center.x - chartSize.width / 2,
                y = 0f,
            ),
            size = chartSize,
        )
    }
}