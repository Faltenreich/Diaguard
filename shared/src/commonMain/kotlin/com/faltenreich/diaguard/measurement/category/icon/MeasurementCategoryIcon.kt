package com.faltenreich.diaguard.measurement.category.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.shared.view.drawText

@Composable
fun MeasurementCategoryIcon(
    category: MeasurementCategory,
    modifier: Modifier = Modifier,
) {
    MeasurementCategoryIcon(
        icon = category.icon,
        fallback = category.name,
        modifier = modifier,
    )
}

@Composable
fun MeasurementCategoryIcon(
    icon: String?,
    fallback: String,
    modifier: Modifier = Modifier,
) {
    val text = icon
        ?: fallback.firstOrNull()?.uppercase()
        ?: "?"
    Box(
        modifier = modifier
            .size(AppTheme.dimensions.size.ImageMedium)
            .background(
                // TODO: Generate color from "fallback"
                color = if (icon != null) Color.Transparent else AppTheme.colors.scheme.onSurface,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
            color = AppTheme.colors.scheme.inverseOnSurface,
        )
    }
}

@Suppress("FunctionName")
fun DrawScope.MeasurementCategoryIcon(
    icon: String?,
    fallback: String,
    position: Offset,
    size: Size,
    fontSize: Float,
    fontPaint: Paint,
    textMeasurer: TextMeasurer,
) {
    val text = icon
        ?: fallback.firstOrNull()?.uppercase()
        ?: "?"
    val textSize = textMeasurer.measure(
        text = text,
        //style = TextStyle(fontSize = TextUnit(value = fontSize, type = TextUnitType.Em)),
    )
    val hasIcon = icon != null

    if (!hasIcon) {
        val padding = 12f
        val path = Path()
        val rect = Rect(
            left = position.x + padding,
            top = position.y + padding,
            right = position.x + size.width - padding,
            bottom = position.y + size.height - padding,
        )
        path.addOval(rect)
        drawPath(
            path = path,
            color = Color.DarkGray,
        )
    }

    drawText(
        text = text,
        x = position.x + size.width / 2 - textSize.size.width / 2,
        y = position.y + size.height / 2 + textSize.size.height / 2 - 8, // TODO: Remove magic offset
        size = fontSize,
        paint = fontPaint,
    )
}