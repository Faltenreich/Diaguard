package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitList
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeForm
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.DropdownButton
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.values
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    val state = viewModel.collectState()

    AnimatedVisibility(
        visible = state != null,
        enter = fadeIn(),
    ) {
        state ?: return@AnimatedVisibility

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            TextInput(
                input = viewModel.propertyName.collectAsState().value,
                onInputChange = { viewModel.propertyName.value = it },
                label = getString(Res.string.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimensions.padding.P_2,
                        vertical = AppTheme.dimensions.padding.P_3,
                    ),
            )

            if (state.units.isNotEmpty()) {
                MeasurementUnitList(
                    items = state.units,
                    onIntent = { intent -> viewModel.dispatchIntent(intent) },
                )
            } else {
                TextInput(
                    input = viewModel.unitName.collectAsState().value,
                    onInputChange = { viewModel.unitName.value = it },
                    label = getString(Res.string.measurement_unit),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.dimensions.padding.P_2,
                            vertical = AppTheme.dimensions.padding.P_3,
                        ),
                )
            }

            TextDivider(getString(Res.string.values))

            FormRow {
                Text(stringResource(Res.string.aggregation_style))
                Spacer(modifier = Modifier.weight(1f))
                DropdownButton(
                    text = stringResource(viewModel.aggregationStyle.value.labelResource),
                    items = MeasurementAggregationStyle.entries.map { aggregationStyle ->
                        DropdownTextMenuItem(
                            label = stringResource(aggregationStyle.labelResource),
                            onClick = { viewModel.aggregationStyle.value = aggregationStyle },
                            isSelected = { viewModel.aggregationStyle.value == aggregationStyle },
                        )
                    },
                )
            }

            Divider()

            MeasurementValueRangeForm(
                unitName = state.unitName,
                viewModel = viewModel,
            )
        }
    }
}