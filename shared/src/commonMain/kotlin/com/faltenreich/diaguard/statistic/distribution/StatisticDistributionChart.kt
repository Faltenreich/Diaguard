package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.center
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.drawText
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries
import org.jetbrains.compose.resources.stringResource

private const val ANGLE_CIRCULAR = 360f

@Composable
fun StatisticDistributionChart(
    state: StatisticDistributionState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val fontPaint = Paint().apply { color = colorScheme.onSurface }
    val fontSize = density.run { AppTheme.typography.bodyMedium.fontSize.toPx() }

    val emptyLabel = stringResource(Res.string.no_entries)

    val colorDefault = AppTheme.colors.scheme.surfaceContainerLow
    val colorByTint = MeasurementValueTint.entries.associateWith { it.getColor() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val chartSize = Size(size.height, size.height)
        val chartTopLeft = Offset(
            x = center.x - chartSize.width / 2,
            y = 0f,
        )
        var startAngle = 0f
        if (state.parts.isNotEmpty()) {
            state.parts.forEach { part ->
                val sweepAngle = part.percentage * ANGLE_CIRCULAR
                drawValue(
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    label = part.label,
                    color = colorByTint[part.tint] ?: colorDefault,
                    topLeft = chartTopLeft,
                    size = chartSize,
                    textMeasurer = textMeasurer,
                    fontSize = fontSize,
                    fontPaint = fontPaint,
                )
                startAngle += sweepAngle
            }
        } else {
            drawValue(
                startAngle = startAngle,
                sweepAngle = ANGLE_CIRCULAR,
                label = emptyLabel,
                color = colorDefault,
                topLeft = chartTopLeft,
                size = chartSize,
                textMeasurer = textMeasurer,
                fontSize = fontSize,
                fontPaint = fontPaint,
            )
        }
    }
}

private fun DrawScope.drawValue(
    startAngle: Float,
    sweepAngle: Float,
    label: String,
    color: Color,
    topLeft: Offset,
    size: Size,
    textMeasurer: TextMeasurer,
    fontSize: Float,
    fontPaint: Paint,
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = topLeft,
        size = size,
    )
    val textSize = textMeasurer.measure(label)
    drawText(
        text = label,
        x = topLeft.x + size.center.x - textSize.size.center.x,
        y = topLeft.y + size.center.y + textSize.size.center.y,
        size = fontSize,
        paint = fontPaint,
    )
}