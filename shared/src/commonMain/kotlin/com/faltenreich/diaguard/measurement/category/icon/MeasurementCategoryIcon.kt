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
import com.faltenreich.diaguard.shared.theme.color.asColor
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
    val char = fallback.firstOrNull()?.uppercaseChar() ?: '?'
    val text = icon ?: char.toString()
    Box(
        modifier = modifier
            .size(AppTheme.dimensions.size.ImageMedium)
            .background(
                color = if (icon != null) Color.Transparent else char.asColor(),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
            color = Color.White,
        )
    }
}

@Suppress("FunctionName")
fun DrawScope.MeasurementCategoryIcon(
    icon: String?,
    fallback: String,
    position: Offset,
    size: Size,
    textMeasurer: TextMeasurer,
) {
    val char = fallback.firstOrNull()?.uppercaseChar() ?: '?'
    val text = icon ?: char.toString()
    val textSize = textMeasurer.measure(text)
    val padding = 12f

    val hasIcon = icon != null

    if (!hasIcon) {
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
            color = char.asColor(),
        )
    }

    // TODO: Remove magic offsets
    drawText(
        text = text,
        x = position.x + size.width / 2 - textSize.size.width / 2,
        y = position.y + size.height / 2 + textSize.size.height / 2 - 8,
        size = textSize.size.height.toFloat() - padding / 2,
        paint = Paint().apply { color = Color.White },
    )
}