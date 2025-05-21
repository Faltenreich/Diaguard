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
    val valueDotRadius = 12f

    Canvas(modifier = modifier.fillMaxSize()) {
        val widthPerDay = size.width / days.size
        days.forEachIndexed { index, day ->
            val rectangle = Rect(
                offset = Offset(
                    x = index * widthPerDay,
                    y = 0f,
                ),
                size = Size(
                    width = widthPerDay,
                    height = size.height,
                ),
            )
            drawDayOfWeek(
                day = day,
                index = index,
                widthPerDay = widthPerDay,
                textMeasurer = textMeasurer,
                fontSize = fontSize,
                fontPaint = fontPaint,
            )
            day.average?.let { value ->
                drawValue(
                    value = value,
                    maximum = maximumValue,
                    rectangle = rectangle,
                    radius = valueDotRadius,
                )
            }
        }
    }
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

private fun DrawScope.drawDayOfWeek(
    day: DashboardState.Trend.Day,
    index: Int,
    widthPerDay: Float,
    textMeasurer: TextMeasurer,
    fontSize: Float,
    fontPaint: Paint,
) {
    val text = day.date
    val textSize = textMeasurer.measure(text).size
    val x = (widthPerDay * index) + (widthPerDay / 2) - (textSize.width / 2)
    val y = size.height - (textSize.height / 2)
    drawText(
        text = text,
        x = x,
        y = y,
        size = fontSize,
        paint = fontPaint,
    )
}