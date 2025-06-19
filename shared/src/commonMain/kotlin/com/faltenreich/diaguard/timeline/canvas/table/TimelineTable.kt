package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineTable(
    state: TimelineTableState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) = with(state) {
    categories.forEachIndexed { categoryIndex, category ->
        category.properties.forEachIndexed { propertyIndex, property ->
            val isFirstRow = categoryIndex == 0 && propertyIndex == 0
            if (!isFirstRow) {
                // Divider
                drawLine(
                    color = config.gridStrokeColor,
                    start = property.rectangle.topLeft,
                    end = property.rectangle.topRight,
                    strokeWidth = config.gridStrokeWidth,
                )
            }

            val iconSize = property.rectangle.height

            // Label background
            drawRoundRect(
                color = config.backgroundColor,
                topLeft = Offset(
                    x = property.rectangle.left + config.padding / 2,
                    y = property.rectangle.top + config.padding / 2,
                ),
                size = Size(
                    width = iconSize + textMeasurer.measure(property.name).size.width + config.padding / 2,
                    height = property.rectangle.height - config.padding,
                ),
                cornerRadius = config.cornerRadius,
            )

            // FIXME: Icon and label have vertical offset

            MeasurementCategoryIcon(
                category = category.category,
                topLeft = property.rectangle.topLeft,
                size = Size(width = iconSize, height = iconSize),
                textMeasurer = textMeasurer,
            )

            // Label
            drawText(
                text = property.name,
                bottomLeft = Offset(
                    x = property.rectangle.left + iconSize,
                    y = property.rectangle.center.y + config.fontSize / 2, // FIXME: Has small vertical offset
                ),
                size = config.fontSize,
                paint = config.fontPaint,
            )

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