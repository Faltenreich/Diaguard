package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineChartLabels(
    state: TimelineChartState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    val iconSize = 80f // TODO: Get from single source of truth, e.g. TimelineConfig
    // TODO: Add background and encapsulate for TimelineTable
    MeasurementCategoryIcon(
        category = property.category,
        topLeft = Offset(
            x = rectangle.left,
            y = rectangle.bottom - iconSize - config.padding / 2,
        ),
        size = Size(width = iconSize, height = iconSize),
        textMeasurer = textMeasurer,
    )

    val heightPerSection = (rectangle.size.height / (valueAxis.last() / valueStep)).toInt()
    valueAxis
        .drop(1)
        .dropLast(1)
        .forEach { value ->
            val index = valueAxis.indexOf(value)
            val y = rectangle.bottom - (index * heightPerSection)

            // Line
            drawLine(
                color = config.gridStrokeColor,
                start = Offset(
                    x = rectangle.left,
                    y = y,
                ),
                end = Offset(
                    x = rectangle.right,
                    y = y,
                ),
                strokeWidth = config.gridStrokeWidth,
            )

            // TODO: Map to MeasurementValueForUser
            val text = value.toString()
            val textSize = textMeasurer.measure(text)

            // Background
            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = rectangle.left + config.padding - config.padding / 2,
                    top = y - textSize.size.height - config.padding / 2,
                    right = rectangle.left + config.padding + textSize.size.width + config.padding,
                    bottom = y - config.padding / 2,
                ),
                cornerRadius = config.cornerRadius,
            )
            path.addRoundRect(rect)
            drawPath(
                path = path,
                color = config.backgroundColor,
            )

            // Text
            drawText(
                text = text,
                bottomLeft = Offset(
                    x = rectangle.left + config.padding,
                    y = y - config.padding,
                ),
                size = config.fontSize,
                paint = config.fontPaint,
            )
        }
}