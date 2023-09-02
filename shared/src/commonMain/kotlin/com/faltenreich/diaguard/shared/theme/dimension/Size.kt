package com.faltenreich.diaguard.shared.theme.dimension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val ImageSmaller: Dp = 18.dp,
    val ImageSmall: Dp = 24.dp,
    val ImageMedium: Dp = 28.dp,
    val ImageLarge: Dp = 48.dp,
    val ImageXLarge: Dp = 64.dp,
    val TouchSizeMinimum: Dp = 48.dp,
    val TouchSizeMedium: Dp = 56.dp,
    val TouchSizeLarge: Dp = 64.dp,
    val ListItemHeightMinimum: Dp = 80.dp,
    val ListOffsetWidth: Dp = 60.dp,
)