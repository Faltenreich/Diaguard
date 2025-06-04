package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
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
    val textMeasurer = rememberTextMeasurer()
    val fontSize = AppTheme.typography.bodyMedium.fontSize
    val fontPaint = Paint().apply { color = colorScheme.onSurface }
    val colorByTint = MeasurementValueTint.entries.associateWith { it.getColor() }

    Canvas(modifier = modifier.fillMaxSize()) {
        var startAngle = 0f
        state.parts.forEach { part ->
            val sweepAngle = part.percentage * ANGLE_CIRCULAR
            val size = Size(size.height, size.height)
            drawValue(
                rectangle = Rect(
                    offset = Offset(x = center.x - size.center.x, y = 0f),
                    size = size,
                ),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                color = colorByTint[part.tint] ?: Color.Transparent,
                text = part.label,
                textSize = fontSize,
                textPaint = fontPaint,
                textMeasurer = textMeasurer,
            )
            startAngle += sweepAngle
        }
    }
}

private fun DrawScope.drawValue(
    rectangle: Rect,
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    text: String,
    textSize: TextUnit,
    textPaint: Paint,
    textMeasurer: TextMeasurer,
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = rectangle.topLeft,
        size = rectangle.size,
    )

    val textLayoutSize = textMeasurer.measure(text, style = TextStyle(fontSize = textSize)).size
    val textAngle = (startAngle + sweepAngle / 2) * (PI.toFloat() / (ANGLE_CIRCULAR / 2))
    val radius = rectangle.size.center.x

    val x = rectangle.center.x + (radius * LABEL_DISTANCE * cos(textAngle)) - textLayoutSize.center.x
    val y = rectangle.center.y + (radius * LABEL_DISTANCE * sin(textAngle)) + textLayoutSize.center.y

    drawText(
        text = text,
        bottomLeft = Offset(x, y),
        size = textSize.toPx(),
        paint = textPaint,
    )
}