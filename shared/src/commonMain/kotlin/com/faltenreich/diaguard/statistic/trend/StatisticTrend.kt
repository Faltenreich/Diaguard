package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.dashboard.trend.DashboardTrendChart

@Composable
fun StatisticTrend(
    state: DashboardState.Trend,
    modifier: Modifier = Modifier,
) {
    DashboardTrendChart(
        state = state,
        modifier = modifier
            .height(AppTheme.dimensions.size.TrendHeight)
            .padding(AppTheme.dimensions.padding.P_3),
    )
}