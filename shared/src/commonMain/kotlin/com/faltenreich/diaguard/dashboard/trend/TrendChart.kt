package com.faltenreich.diaguard.dashboard.trend

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
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.shared.view.drawText

@Composable
fun TrendChart(
    state: DashboardState.Trend,
    modifier: Modifier = Modifier,
) = with(state) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val typography = AppTheme.typography
    val fontPaint = Paint().apply { color = colorScheme.onSurfaceVariant }
    val fontSize = density.run { typography.bodyMedium.fontSize.toPx() }
    val valueDotRadius = 8f

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
                    y = 0f,
                ),
                size = Size(
                    width = widthPerDay,
                    height = size.height - textHeight,
                ),
            )
            drawTarget(
                target = targetValue,
                maximum = maximumValue,
                rectangle = chartRectangle,
            )
            day.average?.let { value ->
                drawValue(
                    value = value,
                    maximum = maximumValue,
                    rectangle = chartRectangle,
                    radius = valueDotRadius,
                )
            }
        }
    }
}

private fun DrawScope.drawLabel(
    day: DashboardState.Trend.Day,
    rectangle: Rect,
    fontSize: Float,
    fontPaint: Paint,
    textMeasurer: TextMeasurer,
) {
    val text = day.date
    val textSize = textMeasurer.measure(text).size
    val x = rectangle.center.x - (textSize.width / 2)
    val y = size.height - (textSize.height / 2)
    drawText(
        text = text,
        x = x,
        y = y,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawTarget(
    target: Double,
    maximum: Double,
    rectangle: Rect,
) {
    val y = rectangle.height - (rectangle.height * (target / maximum).toFloat())
    drawLine(
        color = Color.Gray,
        start = Offset(x = rectangle.left, y = y),
        end = Offset(x = rectangle.right, y = y),
    )
}

private fun DrawScope.drawValue(
    value: Double,
    maximum: Double,
    rectangle: Rect,
    radius: Float,
) {
    val position = Offset(
        x = rectangle.center.x,
        y = rectangle.height - (rectangle.height * (value / maximum).toFloat()),
    )
    drawCircle(
        color = Color.White, // TODO
        radius = radius,
        center = position,
        style = Fill,
    )
}