package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeForm
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style
import diaguard.shared.generated.resources.aggregation_style_description
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.measurement_unit_selected_description
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.values
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementPropertyForm(
    viewModel: MeasurementPropertyFormViewModel,
    modifier: Modifier = Modifier,
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
                        start = AppTheme.dimensions.padding.P_0,
                        top = AppTheme.dimensions.padding.P_3,
                        end = AppTheme.dimensions.padding.P_0,
                        bottom = AppTheme.dimensions.padding.P_0,
                    ),
            )

            if (state.units.isNotEmpty()) {
                TextDivider(getString(Res.string.measurement_unit))
                UnitList(state, onIntent = viewModel::dispatchIntent)
            } else {
                Divider()
                UnitInput(
                    input = viewModel.unitName.collectAsState().value,
                    onInputChange = { viewModel.unitName.value = it },
                )
            }

            TextDivider(getString(Res.string.values))

            FormRow(modifier = Modifier.clickable { TODO() }) {
                Column(modifier = Modifier.weight(AppTheme.dimensions.weight.W_1)) {
                    Text(stringResource(Res.string.aggregation_style))
                    Text(
                        text = stringResource(Res.string.aggregation_style_description),
                        style = AppTheme.typography.bodySmall,
                    )
                }
                Text(stringResource(viewModel.aggregationStyle.collectAsState().value.labelResource))
            }

            Divider()

            MeasurementValueRangeForm(
                unitName = viewModel.unitName.collectAsState().value,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun UnitList(
    state: MeasurementPropertyFormState,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.units.forEachIndexed { index, item ->
            if (index != 0) {
                Divider()
            }
            FormRow(
                modifier = modifier.clickable {
                    onIntent(MeasurementPropertyFormIntent.SelectUnit(item.unit))
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title)
                        item.subtitle?.let { subtitle ->
                            Text(
                                text = subtitle,
                                style = AppTheme.typography.bodySmall,
                            )
                        }
                    }
                    AnimatedVisibility(visible = item.isSelected) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_check),
                            contentDescription = getString(
                                Res.string.measurement_unit_selected_description
                            ),
                            modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
                            tint = AppTheme.colors.scheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitInput(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = input,
        onInputChange = onInputChange,
        label = getString(Res.string.measurement_unit),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimensions.padding.P_1,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
    )
}