package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun StatisticDistribution(
    state: StatisticDistributionState?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppTheme.dimensions.size.StatisticTrendHeight)
            .padding(AppTheme.dimensions.padding.P_3),
    ) {
        state ?: return
        StatisticDistributionChart(
            state = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}