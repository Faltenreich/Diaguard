package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(AppTheme.colors.scheme.error))
}

@Composable
fun Modifier.skeleton(show: Boolean): Modifier {
    val color = Color.LightGray
    val cornerRadius = AppTheme.dimensions.size.CornerRadius
    return drawWithCache {
        onDrawWithContent {
            if (show)
            drawRoundRect(
                color = color,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = cornerRadius,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    Skeleton()
}