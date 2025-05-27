package com.faltenreich.diaguard.statistic.daterange

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_range_picker_open
import diaguard.shared.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.ic_arrow_up
import diaguard.shared.generated.resources.ic_time
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticDateRange(
    state: StatisticDateRangeState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Type(state, onIntent)
        Value(state, onIntent)
    }
}

@Composable
private fun Type(
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

@Composable
private fun Value(
    state: StatisticDateRangeState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
            )
        }
        Text(state.title)
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_up),
                contentDescription = null,
            )
        }
    }
}