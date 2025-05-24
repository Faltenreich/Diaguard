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
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.statistic.StatisticState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries

@Composable
fun StatisticDistribution(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(modifier = modifier) {
        properties.forEachIndexed { index, property ->
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_3),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            ) {
                if (property.property.isNamedUniquely) {
                    Text(property.property.name)
                }
                if (property.parts.isNotEmpty()) {
                    StatisticDistributionChart(
                        state = property,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Text(
                        text = getString(Res.string.no_entries),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            if (index != properties.lastIndex) {
                Divider()
            }
        }
    }
}