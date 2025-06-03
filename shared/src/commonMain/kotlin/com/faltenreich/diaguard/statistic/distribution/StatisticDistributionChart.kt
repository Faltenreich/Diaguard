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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val ANGLE_CIRCULAR = 360f
private const val LABEL_DISTANCE = .5f

@Composable
fun StatisticDistributionChart(
    state: StatisticDistributionState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val fontPaint = Paint().apply { color = colorScheme.onSurface }
    val fontSize = density.run { AppTheme.typography.bodyMedium.fontSize.toPx() }
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
                label = part.label,
                color = colorByTint[part.tint] ?: Color.Transparent,
                topLeft = chartTopLeft,
                size = chartSize,
                textMeasurer = textMeasurer,
                fontSize = fontSize,
                fontPaint = fontPaint,
            )
            startAngle += sweepAngle
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

    val radius = size.width / 2
    val centerX = topLeft.x + size.center.x
    val centerY = topLeft.y + size.center.y
    val textAngle = (startAngle + sweepAngle / 2) * (PI.toFloat() / (ANGLE_CIRCULAR / 2))

    val x = (centerX + (radius * LABEL_DISTANCE * cos(textAngle.toDouble()))).toFloat()
    val y = (centerY + (radius * LABEL_DISTANCE * sin(textAngle.toDouble()))).toFloat()

    drawText(
        text = label,
        bottomLeft = Offset(
            x = x - textSize.size.center.x,
            y = y + textSize.size.center.y,
        ),
        size = fontSize,
        paint = fontPaint,
    )
}