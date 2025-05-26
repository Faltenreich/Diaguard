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
import diaguard.shared.generated.resources.measurement_value
import diaguard.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticAverage(
    state: StatisticAverageState?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow {
            Text(
                text = getString(Res.string.entries_per_day),
                modifier = Modifier.weight(1f),
            )
            if (state != null) {
                Text(state.countPerDay)
            }
        }
        Divider()
        FormRow {
            Text(
                text = stringResource(Res.string.measurement_value),
                modifier = Modifier.weight(1f),
            )
            if (state != null) {
                Text(state.value ?: getString(Res.string.placeholder))
            }
        }
    }
}