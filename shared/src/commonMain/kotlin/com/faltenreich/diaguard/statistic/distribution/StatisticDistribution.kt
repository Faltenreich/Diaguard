package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries

@Composable
fun StatisticDistribution(
    state: StatisticDistributionState,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(
        modifier = modifier.padding(AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
    ) {
        if (property.isNamedUniquely) {
            Text(property.name)
        }
        if (parts.isNotEmpty()) {
            StatisticDistributionChart(
                state = state,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                text = getString(Res.string.no_entries),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.padding.P_2),
                textAlign = TextAlign.Center,
            )
        }
    }
}