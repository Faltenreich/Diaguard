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
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.center
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.shared.theme.color.asColor

@Composable
fun MeasurementCategoryIcon(
    category: MeasurementCategory,
    modifier: Modifier = Modifier,
) {
    val char = category.name.firstOrNull()?.uppercaseChar() ?: '?'
    val text = category.icon ?: char.toString()
    Box(
        modifier = modifier
            .size(AppTheme.dimensions.size.ImageMedium)
            .background(
                color = if (category.icon != null) Color.Transparent else char.asColor(),
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

@Suppress("FunctionName", "MagicNumber")
fun DrawScope.MeasurementCategoryIcon(
    textMeasurer: TextMeasurer,
    category: MeasurementCategory,
    topLeft: Offset,
    size: Size,
    textStyle: TextStyle,
) {
    val char = category.name.firstOrNull()?.uppercaseChar() ?: '?'
    val text = category.icon ?: char.toString()
    val textSize = textMeasurer.measure(text, textStyle)
    val padding = 0f

    val hasIcon = category.icon != null

    if (!hasIcon) {
        val path = Path()
        val rect = Rect(
            left = topLeft.x + padding,
            top = topLeft.y + padding,
            right = topLeft.x + size.width - padding,
            bottom = topLeft.y + size.height - padding,
        )
        path.addOval(rect)
        drawPath(
            path = path,
            color = char.asColor(),
        )
    }

    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(
            x = topLeft.x + size.center.x - textSize.size.center.x,
            y = topLeft.y + size.center.y - textSize.size.center.y,
        ),
        style = textStyle,
    )
}