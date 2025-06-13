package com.faltenreich.diaguard.timeline.canvas.table

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

            val labelSize = textMeasurer.measure(property.name)

            // Label background
            // TODO: Reuse path
            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = property.rectangle.left + config.padding / 2,
                    top = property.rectangle.top + config.padding / 2,
                    right = property.rectangle.left + iconSize + labelSize.size.width + config.padding / 2,
                    bottom = property.rectangle.bottom - config.padding / 2,
                ),
                cornerRadius = config.cornerRadius,
            )
            path.addRoundRect(rect)
            drawPath(
                path = path,
                color = config.backgroundColor,
            )

            MeasurementCategoryIcon(
                icon = property.icon,
                fallback = property.name,
                position = property.rectangle.topLeft,
                size = Size(width = iconSize, height = iconSize),
                textMeasurer = textMeasurer,
            )

            // Label
            drawText(
                text = property.name,
                bottomLeft = Offset(
                    x = property.rectangle.left + iconSize,
                    y = property.rectangle.top + property.rectangle.height / 2 + config.fontSize / 2,
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