package com.faltenreich.diaguard.statistic.average

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.entries_per_day
import com.faltenreich.diaguard.resource.measurement_value
import com.faltenreich.diaguard.resource.placeholder
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.layout.FormRow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticAverage(
    state: StatisticAverageState?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow {
            Text(
                text = stringResource(Res.string.measurement_value),
                modifier = Modifier.weight(1f),
            )
            if (state != null) {
                Text(state.value ?: stringResource(Res.string.placeholder))
            }
        }
        Divider()
        FormRow {
            Text(
                text = stringResource(Res.string.entries_per_day),
                modifier = Modifier.weight(1f),
            )
            if (state != null) {
                Text(state.countPerDay)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    StatisticAverage(
        state = StatisticAverageState(
            property = property(),
            countPerDay = "countPerDay",
            value = "value",
        ),
    )
}