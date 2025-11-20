package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDistribution(
        state = StatisticDistributionState(
            property = property(),
            parts = listOf(
                StatisticDistributionState.Part(
                    label = "50%",
                    percentage = .5f,
                    tint = MeasurementValueTint.NORMAL,
                ),
                StatisticDistributionState.Part(
                    label = "25%",
                    percentage = .25f,
                    tint = MeasurementValueTint.LOW,
                ),
                StatisticDistributionState.Part(
                    label = "25%",
                    percentage = .25f,
                    tint = MeasurementValueTint.HIGH,
                ),
            ),
        ),
    )
}