package com.faltenreich.diaguard.shared.theme.dimension

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("ConstructorParameterNaming", "PropertyName", "MagicNumber")
data class Size(
    val ImageSmaller: Dp = 18.dp,
    val ImageSmall: Dp = 24.dp,
    val ImageMedium: Dp = 28.dp,
    val ImageLarge: Dp = 36.dp,
    val ImageXLarge: Dp = 64.dp,
    val TouchSizeSmall: Dp = 48.dp,
    val TouchSizeMedium: Dp = 56.dp,
    val TouchSizeLarge: Dp = 64.dp,
    val ListItemHeightMinimum: Dp = 80.dp,
    val ListOffsetWidth: Dp = 60.dp,
    val DashboardTrendHeight: Dp = 100.dp,
    val LogDayWidth: Dp = 60.dp,
    val TimelineTableRowHeight: Dp = 40.dp,
    val StatisticDistributionChartHeight: Dp = 200.dp,
    val StatisticTrendHeight: Dp = 200.dp,
    val CornerRadius: CornerRadius = CornerRadius(x = 12f, y = 12f),
)