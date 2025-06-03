package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun StatisticDistribution(
    state: StatisticDistributionState?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        horizontalAlignment = Alignment.End,
    ) {
        state ?: return
        StatisticDistributionChart(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimensions.size.StatisticTrendHeight),
        )
        StatisticDistributionLegend()
    }
}