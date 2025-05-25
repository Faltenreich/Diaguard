package com.faltenreich.diaguard.statistic.category

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
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_range_picker_open
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticCategory(
    state: StatisticCategoryState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) = with(state) {
    var expandDropDown by remember { mutableStateOf(false) }

    FormRow(
        icon = { MeasurementCategoryIcon(selection) },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = stringResource(Res.string.date_range_picker_open),
                role = Role.Button,
                onClick = { expandDropDown = true },
            ),
    ) {
        Text(selection.name)

        DropdownTextMenu(
            expanded = expandDropDown,
            onDismissRequest = { expandDropDown = false },
            items = categories.map { category ->
                category.name to { onIntent(StatisticIntent.SetCategory(category)) }
            }
        )
    }
}