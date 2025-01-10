package com.faltenreich.diaguard.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.DropdownButton
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.distribution
import diaguard.shared.generated.resources.entries_per_day
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.placeholder
import diaguard.shared.generated.resources.trend

@Composable
fun Statistic(
    modifier: Modifier = Modifier,
    viewModel: StatisticViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { MeasurementCategoryIcon(state.selectedCategory) }) {
            DropdownButton(
                text = state.selectedCategory.name,
                items = state.categories.map { category ->
                    category.name to { viewModel.dispatchIntent(StatisticIntent.Select(category)) }
                }
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_time) }) {
            TextButton(
                onClick = { viewModel.dispatchIntent(StatisticIntent.OpenDateRangePicker) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.scheme.onSurfaceVariant,
                ),
            ) {
                Text(
                    text = viewModel.dateRangeLocalized,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        TextDivider(getString(Res.string.average))
        state.average.values.forEach { value ->
            FormRow {
                Text(value.first.name, modifier = Modifier.weight(1f))
                Text(value.second ?: getString(Res.string.placeholder))
            }
            Divider()
        }
        FormRow {
            Text(getString(Res.string.entries_per_day), modifier = Modifier.weight(1f))
            Text(state.average.countPerDay)
        }

        TextDivider(getString(Res.string.trend))

        TextDivider(getString(Res.string.distribution))
    }
}