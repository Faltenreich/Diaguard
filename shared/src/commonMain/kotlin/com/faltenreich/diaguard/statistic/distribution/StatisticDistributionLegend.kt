package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

@Composable
fun StatisticDistributionLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2)
    ) {
        StatisticDistributionLegendItem(MeasurementValueTint.LOW)
        StatisticDistributionLegendItem(MeasurementValueTint.NORMAL)
        StatisticDistributionLegendItem(MeasurementValueTint.HIGH)
    }
}