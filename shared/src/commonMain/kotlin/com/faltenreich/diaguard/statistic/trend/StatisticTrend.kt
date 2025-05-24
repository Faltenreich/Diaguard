package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun StatisticTrend(
    state: StatisticTrendState,
    modifier: Modifier = Modifier,
) {
    StatisticTrendChart(
        state = state,
        modifier = modifier
            .height(AppTheme.dimensions.size.TrendHeight)
            .padding(AppTheme.dimensions.padding.P_3),
    )
}