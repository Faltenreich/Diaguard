package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
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
            val textSize = textMeasurer.measure(text, config.textStyle).size.toSize()

            // Background
            drawRoundRect(
                color = config.backgroundColor,
                topLeft = Offset(
                    x = chartRectangle.left + config.padding,
                    y = y - textSize.center.y,
                ),
                size = Size(
                    width = textSize.width,
                    height = textSize.height,
                ),
                cornerRadius = config.cornerRadius,
            )

            // Text
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    x = chartRectangle.left + config.padding,
                    y = y - textSize.center.y,
                ),
                style = config.textStyle,
                size = textSize,
            )
        }
}