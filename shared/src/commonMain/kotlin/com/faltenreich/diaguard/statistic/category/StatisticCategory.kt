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
import com.faltenreich.diaguard.view.DropdownTextMenu
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_range_picker_open
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticCategory(
    state: StatisticCategoryState?,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandDropDown by remember { mutableStateOf(false) }

    FormRow(
        icon = {
            state?.selection?.let { category ->
                MeasurementCategoryIcon(category)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = stringResource(Res.string.date_range_picker_open),
                role = Role.Button,
                onClick = { expandDropDown = true },
            ),
    ) {
        state ?: return@FormRow
        Text(state.selection.name)

        DropdownTextMenu(
            expanded = expandDropDown,
            onDismissRequest = { expandDropDown = false },
            items = state.categories.map { category ->
                category.name to { onIntent(StatisticIntent.SetCategory(category)) }
            }
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val categories = listOf(
        category(),
        category(),
    )
    StatisticCategory(
        state = StatisticCategoryState(
            categories = categories,
            selection = categories.first(),
        ),
        onIntent = {},
    )
}