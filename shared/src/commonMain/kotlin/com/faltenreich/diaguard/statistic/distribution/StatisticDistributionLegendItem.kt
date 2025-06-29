package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticDistributionLegendItem(
    tint: MeasurementValueTint,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.dimensions.size.ImageSmaller)
                .background(
                    color = tint.getColor(),
                    shape = AppTheme.shapes.extraSmall,
                ),
        )
        Text(
            text = tint.getTitle(),
            style = AppTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDistributionLegendItem(
        tint = MeasurementValueTint.NORMAL,
    )
}