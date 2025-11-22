package com.faltenreich.diaguard.statistic.daterange

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.view.DropdownTextMenu
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.view.ResourceIcon
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_range_picker_open
import diaguard.shared.generated.resources.ic_time
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticDateRangeButton(
    state: StatisticDateRangeState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandDropDown by remember { mutableStateOf(false) }

    FormRow(
        icon = { ResourceIcon(Res.drawable.ic_time) },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = stringResource(Res.string.date_range_picker_open),
                role = Role.Button,
                onClick = { expandDropDown = true },
            ),
    ) {
        Text(stringResource(state.type.labelResource))

        DropdownTextMenu(
            expanded = expandDropDown,
            onDismissRequest = { expandDropDown = false },
            items = StatisticDateRangeType.entries.map {
                stringResource(it.labelResource) to { onIntent(StatisticIntent.SetDateRangeType(it)) }
            },
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDateRangeButton(
        state = StatisticDateRangeState(
            type = StatisticDateRangeType.WEEK,
            dateRange = today() .. today(),
            title = "title",
            subtitle = "subtitle",
        ),
        onIntent = {},
    )
}