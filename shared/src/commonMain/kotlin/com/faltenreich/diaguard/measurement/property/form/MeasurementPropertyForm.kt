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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionEvent
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionViewModel
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeForm
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style
import diaguard.shared.generated.resources.aggregation_style_description
import diaguard.shared.generated.resources.delete_error_pre_defined
import diaguard.shared.generated.resources.delete_title
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.measurement_unit_selected_description
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.ok
import diaguard.shared.generated.resources.values
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementPropertyForm(
    viewModel: MeasurementPropertyFormViewModel,
    unitSelectionViewModel: MeasurementUnitSelectionViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()

    LaunchedEffect(Unit) {
        unitSelectionViewModel.collectEvents { event ->
            when (event) {
                is MeasurementUnitSelectionEvent.Select ->
                    viewModel.dispatchIntent(MeasurementPropertyFormIntent.SelectUnit(event.unit))
            }
        }
    }

    AnimatedVisibility(
        visible = state != null,
        enter = fadeIn(),
    ) {
        state ?: return@AnimatedVisibility

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            var name by remember { mutableStateOf(state.property.name) }
            TextInput(
                input = name,
                onInputChange = { input ->
                    name = input
                    viewModel.dispatchIntent(MeasurementPropertyFormIntent.UpdateProperty(name = input))
                },
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

            if (state.unitSuggestions.isNotEmpty()) {
                TextDivider(getString(Res.string.measurement_unit))
                UnitList(state, onIntent = viewModel::dispatchIntent)
            } else {
                Divider()
                UnitButton(
                    unit = state.unit,
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.OpenUnitSearch) },
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
                Text(stringResource(state.property.aggregationStyle.labelResource))
            }

            Divider()

            MeasurementValueRangeForm(
                state = state.valueRange,
                onUpdate = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.UpdateValueRange(it)) },
            )
        }
    }

    if (state?.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.CloseDeleteDialog) },
            onConfirmRequest = {
                viewModel.dispatchIntent(MeasurementPropertyFormIntent.CloseDeleteDialog)
                viewModel.dispatchIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = false))
            }
        )
    }

    if (state?.alertDialog != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.CloseAlertDialog) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.CloseAlertDialog) },
                ) {
                    Text(
                        text = getString(Res.string.ok),
                        color = AppTheme.colors.scheme.onBackground,
                    )
                }
            },
            title = { Text(stringResource(Res.string.delete_title)) },
            text = { Text(stringResource(Res.string.delete_error_pre_defined)) },
        )
    }
}

@Composable
private fun UnitList(
    state: MeasurementPropertyFormState,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.unitSuggestions.forEachIndexed { index, item ->
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
private fun UnitButton(
    unit: MeasurementUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(Res.string.measurement_unit),
            modifier = Modifier.weight(1f),
        )
        Column {
            Text(
                text = unit.name,
            )
            Text(
                text = unit.abbreviation,
                style = AppTheme.typography.bodySmall,
            )
        }
    }
}