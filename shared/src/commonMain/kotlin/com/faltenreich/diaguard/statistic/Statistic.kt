package com.faltenreich.diaguard.statistic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.picker.DateRangePicker
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
    var showDateRangePicker by remember { mutableStateOf(false) }

    when (val viewState = viewModel.collectState()) {
        null -> Unit
        is StatisticViewState.Loaded -> Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            FormRow(icon = { MeasurementCategoryIcon(viewState.selectedCategory) }) {
                DropdownButton(
                    text = viewState.selectedCategory.name,
                    items = viewState.categories.map { category ->
                        category.name to { viewModel.dispatchIntent(StatisticIntent.Select(category)) }
                    }
                )
            }
            Divider()
            FormRow(icon = { ResourceIcon(Res.drawable.ic_time) }) {
                TextButton(onClick = { showDateRangePicker = true }) {
                    Text(
                        text = viewModel.dateRangeLocalized,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            TextDivider(getString(Res.string.average))
            viewState.average.values.forEach { value ->
                FormRow {
                    Text(value.first.name, modifier = Modifier.weight(1f))
                    Text(value.second ?: getString(Res.string.placeholder))
                }
                Divider()
            }
            FormRow {
                Text(getString(Res.string.entries_per_day), modifier = Modifier.weight(1f))
                Text(viewState.average.countPerDay)
            }

            TextDivider(getString(Res.string.trend))

            TextDivider(getString(Res.string.distribution))
        }
    }

    AnimatedVisibility(
        visible = showDateRangePicker,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        DateRangePicker(
            dateRange = viewModel.dateRange,
            onPick = { dateRange ->
                showDateRangePicker = false
                viewModel.dateRange = dateRange
            }
        )
    }
}