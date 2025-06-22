package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.timeline.TimelineConfig

// FIXME: Text has vertical offset
@Suppress("FunctionName")
fun DrawScope.TimelineTable(
    state: TimelineTableState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    categories.forEach { category ->
        category.properties.forEach { property ->
            // Divider
            drawLine(
                color = config.gridStrokeColor,
                start = property.rectangle.topLeft,
                end = property.rectangle.topRight,
                strokeWidth = config.gridStrokeWidth,
            )

            val iconSize = property.rectangle.height
            val showLabel = category.properties.size > 1
            val labelSize = textMeasurer.measure(property.name, config.textStyle).size.toSize()

            // Label background
            drawRect(
                color = config.backgroundColor,
                topLeft = Offset(
                    x = property.rectangle.left,
                    y = property.rectangle.top + config.gridStrokeWidth,
                ),
                size = Size(
                    width = iconSize + if (showLabel) labelSize.width + config.padding / 2 else 0f,
                    height = property.rectangle.height - config.gridStrokeWidth * 2,
                ),
            )

            MeasurementCategoryIcon(
                textMeasurer = textMeasurer,
                category = category.category,
                topLeft = property.rectangle.topLeft,
                size = Size(width = iconSize, height = iconSize),
                textStyle = config.textStyle,
            )

            // Label
            if (showLabel) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = property.name,
                    topLeft = Offset(
                        x = property.rectangle.left + iconSize,
                        y = property.rectangle.center.y - labelSize.center.y,
                    ),
                    style = config.textStyle,
                    size = labelSize,
                )
            }

            property.values.forEach { value ->
                val text = value.value
                val textSize = textMeasurer.measure(text, config.textStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    topLeft = Offset(
                        x = value.rectangle.center.x - textSize.size.center.x,
                        y = value.rectangle.center.y - textSize.size.center.y,
                    ),
                    style = config.textStyle,
                    size = textSize.size.toSize(),
                )
            }
        }
    }
}