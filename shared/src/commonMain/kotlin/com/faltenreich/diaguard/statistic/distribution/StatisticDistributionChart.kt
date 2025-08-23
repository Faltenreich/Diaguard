package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
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
    val fontColor = AppTheme.colors.scheme.onSurface
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
                fontSize = fontSize,
                fontColor = fontColor,
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
    fontSize: TextUnit,
    fontColor: Color,
    textMeasurer: TextMeasurer,
) {
    val radius = rectangle.size.center.x

    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = rectangle.topLeft,
        size = rectangle.size,
    )

    val textAngle = (startAngle + sweepAngle / 2) * (PI.toFloat() / (ANGLE_CIRCULAR / 2))
    val textStyle = TextStyle(fontSize = fontSize, color = fontColor)
    val textSize = textMeasurer.measure(text, textStyle).size.toSize()
    val textCenter = textSize.center
    val textOffset = if (sweepAngle == ANGLE_CIRCULAR) Offset(
        x = rectangle.center.x - textCenter.x,
        y = rectangle.center.y - textCenter.y,
    ) else Offset(
        x = rectangle.center.x + (radius * LABEL_DISTANCE * cos(textAngle)) - textCenter.x,
        y = rectangle.center.y + (radius * LABEL_DISTANCE * sin(textAngle)) - textCenter.y,
    )

    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = textOffset,
        style = textStyle,
        size = textSize,
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDistributionChart(
        state = StatisticDistributionState(
            property = property(),
            parts = listOf(
                StatisticDistributionState.Part(
                    label = "50%",
                    percentage = .5f,
                    tint = MeasurementValueTint.NORMAL,
                ),
                StatisticDistributionState.Part(
                    label = "25%",
                    percentage = .25f,
                    tint = MeasurementValueTint.LOW,
                ),
                StatisticDistributionState.Part(
                    label = "25%",
                    percentage = .25f,
                    tint = MeasurementValueTint.HIGH,
                ),
            ),
        ),
    )
}