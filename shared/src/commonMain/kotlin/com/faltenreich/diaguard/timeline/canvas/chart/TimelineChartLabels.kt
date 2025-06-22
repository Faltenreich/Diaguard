package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.center
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineChartLabels(
    state: TimelineChartState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    drawRect(
        color = config.backgroundColor,
        topLeft = iconRectangle.topLeft,
        size = iconRectangle.size,
    )
    MeasurementCategoryIcon(
        textMeasurer = textMeasurer,
        category = property.category,
        topLeft = Offset(
            x = iconRectangle.left + config.padding / 2,
            y = iconRectangle.top + config.padding / 2,
        ),
        size = Size(
            width = iconRectangle.width - config.padding,
            height = iconRectangle.height - config.padding,
        ),
        textStyle = config.textStyle,
    )

    val heightPerSection = (chartRectangle.size.height / (valueAxis.last() / valueStep)).toInt()
    valueAxis
        .drop(1)
        .dropLast(1)
        .forEach { value ->
            val index = valueAxis.indexOf(value)
            val y = chartRectangle.bottom - (index * heightPerSection)

            // Line
            drawLine(
                color = config.gridStrokeColor,
                start = Offset(
                    x = chartRectangle.left,
                    y = y,
                ),
                end = Offset(
                    x = chartRectangle.right,
                    y = y,
                ),
                strokeWidth = config.gridStrokeWidth,
            )

            // TODO: Map to MeasurementValueForUser
            val text = value.toString()
            val textSize = textMeasurer.measure(text, style = TextStyle(fontSize = config.fontSize))

            // Background
            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = chartRectangle.left + config.padding - config.padding / 2,
                    top = y - textSize.size.center.y - config.padding / 2,
                    right = chartRectangle.left + config.padding + textSize.size.width + config.padding,
                    bottom = y + textSize.size.center.y + config.padding / 2,
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
                    x = chartRectangle.left + config.padding,
                    y = y + textSize.size.center.y,
                ),
                size = config.fontSizePx,
                paint = config.fontPaint,
            )
        }
}