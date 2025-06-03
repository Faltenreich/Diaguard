package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

private const val ANGLE_CIRCULAR = 360f

@Composable
fun StatisticDistributionChart(
    state: StatisticDistributionState,
    modifier: Modifier = Modifier,
) {
    val colorDefault = AppTheme.colors.scheme.onBackground
    val colorByTint = MeasurementValueTint.entries.associateWith { it.getColor() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val chartSize = Size(size.height, size.height)
        val chartTopLeft = Offset(
            x = center.x - chartSize.width / 2,
            y = 0f,
        )
        var startAngle = 0f
        state.parts.forEach { part ->
            val sweepAngle = part.percentage * ANGLE_CIRCULAR
            drawValue(
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                color = colorByTint[part.tint] ?: colorDefault,
                topLeft = chartTopLeft,
                size = chartSize,
            )
            startAngle += sweepAngle
        }
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