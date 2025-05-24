package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.drawText

const val VALUE_DOT_RADIUS = 12f

@Composable
fun StatisticTrendChart(
    state: StatisticTrendState,
    modifier: Modifier = Modifier,
) = with(state) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val fontPaint = Paint().apply { color = colorScheme.onSurfaceVariant }
    val fontSize = density.run { AppTheme.typography.bodyMedium.fontSize.toPx() }
    val padding = density.run { AppTheme.dimensions.padding.P_2.toPx() }

    val colorTintNone = AppTheme.colors.scheme.onPrimary
    val colorTintLow = AppTheme.colors.ValueLow
    val colorTintNormal = AppTheme.colors.ValueNormal
    val colorTintHigh = AppTheme.colors.ValueHigh
    val getColor = { tint: MeasurementValueTint ->
        when (tint) {
            MeasurementValueTint.NONE -> colorTintNone
            MeasurementValueTint.LOW -> colorTintLow
            MeasurementValueTint.NORMAL -> colorTintNormal
            MeasurementValueTint.HIGH -> colorTintHigh
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val widthPerDay = size.width / days.size
        days.forEachIndexed { index, day ->
            val x = index * widthPerDay
            val textSize = textMeasurer.measure("D")
            val textHeight = textSize.size.height.toFloat()
            val labelRectangle = Rect(
                offset = Offset(
                    x = x,
                    y = size.height - textHeight,
                ),
                size = Size(
                    width = widthPerDay,
                    height = textHeight,
                ),
            )
            drawLabel(
                day = day,
                rectangle = labelRectangle,
                fontSize = fontSize,
                fontPaint = fontPaint,
                textMeasurer = textMeasurer,
            )

            val chartRectangle = Rect(
                offset = Offset(
                    x = x,
                    y = padding,
                ),
                size = Size(
                    width = widthPerDay,
                    height = size.height - textHeight - padding - padding,
                ),
            )
            drawTarget(
                target = targetValue,
                maximum = maximumValue,
                rectangle = chartRectangle,
            )
            day.average?.let { value ->
                drawValue(
                    value = value.value,
                    color = getColor(value.tint),
                    maximum = maximumValue,
                    rectangle = chartRectangle,
                    radius = VALUE_DOT_RADIUS,
                )
            }
        }
    }
}

private fun DrawScope.drawLabel(
    day: StatisticTrendState.Day,
    rectangle: Rect,
    fontSize: Float,
    fontPaint: Paint,
    textMeasurer: TextMeasurer,
) {
    val text = day.date
    val textSize = textMeasurer.measure(text).size
    drawText(
        text = text,
        x = rectangle.center.x - (textSize.width / 2),
        y = rectangle.bottomCenter.y,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawTarget(
    target: Double,
    maximum: Double,
    rectangle: Rect,
) {
    val y = rectangle.top + rectangle.height - (rectangle.height * (target / maximum).toFloat())
    drawLine(
        color = Color.Gray,
        start = Offset(x = rectangle.left, y = y),
        end = Offset(x = rectangle.right, y = y),
    )
}

private fun DrawScope.drawValue(
    value: Double,
    color: Color,
    maximum: Double,
    rectangle: Rect,
    radius: Float,
) {
    val y = rectangle.top + rectangle.height - (rectangle.height * (value / maximum).toFloat())
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(x = rectangle.center.x, y = y),
        style = Fill,
    )
}