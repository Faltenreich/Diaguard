package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.statistic.StatisticState

@Composable
fun StatisticDistribution(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) {
    StatisticDistributionChart(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.padding.P_2),
    )
}