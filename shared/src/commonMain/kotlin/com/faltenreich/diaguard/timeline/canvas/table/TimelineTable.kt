package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.drawText
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
            val labelWidth = textMeasurer.measure(property.name).size.width + config.padding / 2

            // Label background
            drawRect(
                color = config.backgroundColor,
                topLeft = Offset(
                    x = property.rectangle.left,
                    y = property.rectangle.top + config.gridStrokeWidth,
                ),
                size = Size(
                    width = iconSize + if (showLabel) labelWidth else 0f,
                    height = property.rectangle.height - config.gridStrokeWidth * 2,
                ),
            )

            MeasurementCategoryIcon(
                category = category.category,
                topLeft = property.rectangle.topLeft,
                size = Size(width = iconSize, height = iconSize),
                textMeasurer = textMeasurer,
            )

            // Label
            if (showLabel) {
                drawText(
                    text = property.name,
                    bottomLeft = Offset(
                        x = property.rectangle.left + iconSize,
                        y = property.rectangle.center.y + config.fontSize / 2,
                    ),
                    size = config.fontSize,
                    paint = config.fontPaint,
                )
            }

            property.values.forEach { value ->
                val text = value.value
                val textSize = textMeasurer.measure(text)
                val valueX = value.rectangle.center.x - textSize.size.width / 2
                val valueY = value.rectangle.center.y + textSize.size.height / 2
                drawText(
                    text = text,
                    bottomLeft = Offset(
                        x = valueX,
                        y = valueY,
                    ),
                    size = config.fontSize,
                    paint = config.fontPaint,
                )
            }
        }
    }
}