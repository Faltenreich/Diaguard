package com.faltenreich.diaguard.statistic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DateRangePicker
import com.faltenreich.diaguard.shared.view.DropdownButton
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider

@Composable
fun Statistic(
    modifier: Modifier = Modifier,
    viewModel: StatisticViewModel = inject(),
) {
    var showDateRangePicker by remember { mutableStateOf(false) }

    when (val viewState = viewModel.viewState.collectAsState().value) {
        is StatisticViewState.Loading -> Unit
        is StatisticViewState.Loaded -> Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            FormRow(icon = { MeasurementPropertyIcon(viewState.selectedProperty) }) {
                DropdownButton(
                    text = viewState.selectedProperty.name,
                    items = viewState.properties.map { property ->
                        DropdownTextMenuItem(
                            label = property.name,
                            onClick = { viewModel.selectProperty(property) },
                            isSelected = { viewState.selectedProperty == property },
                        )
                    }
                )
            }
            Divider()
            FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
                TextButton(onClick = { showDateRangePicker = true }) {
                    Text(
                        text = viewModel.dateRangeLocalized,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            TextDivider(getString(MR.strings.average))
            FormRow {
                Text(getString(MR.strings.measurement_value), modifier = Modifier.weight(1f))
                Text(viewState.average.value)
            }
            Divider()
            FormRow {
                Text(getString(MR.strings.entries_per_day), modifier = Modifier.weight(1f))
                Text(viewState.average.countPerDay)
            }

            TextDivider(getString(MR.strings.trend))

            TextDivider(getString(MR.strings.distribution))
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