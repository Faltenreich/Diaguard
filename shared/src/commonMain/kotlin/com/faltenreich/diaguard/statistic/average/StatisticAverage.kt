package com.faltenreich.diaguard.statistic.average

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entries_per_day
import diaguard.shared.generated.resources.placeholder

@Composable
fun StatisticAverage(
    state: StatisticAverageState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow {
            Text(state.property.name, modifier = Modifier.weight(1f))
            Text(state.value ?: getString(Res.string.placeholder))
        }
        Divider()
        FormRow {
            Text(getString(Res.string.entries_per_day), modifier = Modifier.weight(1f))
            Text(state.countPerDay)
        }
    }
}