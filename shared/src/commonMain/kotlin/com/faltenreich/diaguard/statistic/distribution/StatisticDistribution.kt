package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            .height(200.dp),
    )
}