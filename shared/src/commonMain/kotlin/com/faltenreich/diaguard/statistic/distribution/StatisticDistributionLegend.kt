package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDistributionLegend()
}