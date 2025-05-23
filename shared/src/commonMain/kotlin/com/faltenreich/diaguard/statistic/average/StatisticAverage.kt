package com.faltenreich.diaguard.statistic.average

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.statistic.StatisticState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entries_per_day
import diaguard.shared.generated.resources.placeholder

@Composable
fun StatisticAverage(
    state: StatisticState.Average,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.values.forEach { value ->
            FormRow {
                Text(value.first.name, modifier = Modifier.weight(1f))
                Text(value.second ?: getString(Res.string.placeholder))
            }
            Divider()
        }
        FormRow {
            Text(getString(Res.string.entries_per_day), modifier = Modifier.weight(1f))
            Text(state.countPerDay)
        }
    }
}