@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.timeline.canvas.time

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineTime(
    state: TimelineTimeState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    drawRect(
        color = config.gridShadowColor,
        topLeft = rectangle.topLeft,
        size = rectangle.size,
        style = Fill,
    )
    state.hours.forEach { hour ->
        if (hour.hour == 0) {
            drawDateIndicator(hour.x, rectangle, config)
        }
        drawHour(hour.x, hour.hour, rectangle, config, textMeasurer)
    }
}

private fun DrawScope.drawDateIndicator(
    x: Float,
    rectangle: Rect,
    config: TimelineConfig,
) {
    drawLine(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            .25f to config.gridShadowColor, // TODO: Calculate stops for gradient
        ),
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = rectangle.bottom - rectangle.height),
        strokeWidth = config.gridStrokeWidth * 20,
    )
}

private fun DrawScope.drawHour(
    x: Float,
    hour: Int,
    rectangle: Rect,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val text = hour.toString()
    val textSize = textMeasurer.measure(text)

    drawLine(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            .15f to config.gridStrokeColor, // TODO: Calculate stops for gradient
        ),
        start = Offset(
            x = x,
            y = 0f,
        ),
        end = Offset(
            x = x,
            y = rectangle.bottom - rectangle.height,
        ),
        strokeWidth = config.gridStrokeWidth,
    )

    drawText(
        text = hour.toString(),
        bottomLeft = Offset(
            x = x - (textSize.size.width / 2),
            y = rectangle.top + rectangle.height / 2 + (textSize.size.height / 2) - (config.padding / 3),
        ),
        size = config.fontSize,
        paint = config.fontPaint,
    )
}