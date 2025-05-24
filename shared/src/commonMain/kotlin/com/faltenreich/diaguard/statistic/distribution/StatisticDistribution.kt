package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.statistic.StatisticState

@Composable
fun StatisticDistribution(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(modifier = modifier) {
        properties.forEachIndexed { index, property ->
            Box(modifier = Modifier.padding(AppTheme.dimensions.padding.P_3)) {
                StatisticDistributionChart(
                    state = property,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(property.property.name)
            }
            if (index != properties.lastIndex) {
                Divider()
            }
        }
    }
}