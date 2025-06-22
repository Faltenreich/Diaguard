@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.timeline.canvas.time

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.center
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineTime(
    state: TimelineTimeState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    drawRect(
        color = config.gridShadowColor,
        topLeft = dimensions.time.topLeft,
        size = dimensions.time.size,
        style = Fill,
    )

    state.hours.forEach { hour ->
        translate(left = hour.x) {
            if (hour.hour == 0) {
                drawLine(
                    color = config.gridShadowColor,
                    start = Offset.Zero,
                    end = Offset(
                        x = 0f,
                        y = dimensions.time.top,
                    ),
                    strokeWidth = config.gridStrokeWidth * 20,
                )
            }

            drawLine(
                color = config.gridStrokeColor,
                start = Offset.Zero,
                end = Offset(
                    x = 0f,
                    y = dimensions.time.top,
                ),
                strokeWidth = config.gridStrokeWidth,
            )

            val text = hour.hour.toString()
            val textSize = textMeasurer.measure(text, config.textStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    x = 0f - textSize.size.center.x,
                    y = dimensions.time.center.y - textSize.size.center.y,
                ),
                style = config.textStyle,
            )
        }
    }
}