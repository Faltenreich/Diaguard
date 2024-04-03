package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)

        is MeasurementTypeFormViewState.Loaded -> {
            LazyColumn(modifier = modifier) {
                item {
                    TextInput(
                        input = viewModel.typeName.collectAsState().value,
                        onInputChange = {
                            viewModel.handleIntent(MeasurementTypeFormIntent.EditTypeName(it))
                        },
                        label = getString(MR.strings.name),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppTheme.dimensions.padding.P_3),
                    )
                }

                if (viewState.type.property.isUserGenerated) {
                    item {
                        TextInput(
                            input = viewModel.unitName.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditUnitName(it))
                            },
                            label = getString(MR.strings.measurement_unit),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.dimensions.padding.P_3),
                        )
                    }
                } else {
                    MeasurementUnitList(units = viewState.type.units)
                }

                stickyHeader { TextDivider(getString(MR.strings.values)) }

                item {
                    Column {

                        TextCheckbox(
                            title = stringResource(MR.strings.value_range_highlighted),
                            subtitle = stringResource(MR.strings.value_range_highlighted_description),
                            checked = viewModel.isValueRangeHighlighted.collectAsState().value,
                            onCheckedChange = {
                                viewModel.dispatchIntent(MeasurementTypeFormIntent.EditIsValueRangeHighlighted(it))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                        )

                        Divider()

                        TextInput(
                            input = viewModel.valueRangeMinimum.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeMinimum(it))
                            },
                            label = getString(MR.strings.value_range_minimum),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            suffix = { Text(viewState.unitName) },
                            supportingText = { Text(stringResource(MR.strings.value_range_minimum_description)) },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )

                        Divider()

                        TextInput(
                            input = viewModel.valueRangeLow.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeLow(it))
                            },
                            label = getString(MR.strings.value_range_low),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            suffix = { Text(viewState.unitName) },
                            supportingText = { Text(stringResource(MR.strings.value_range_low_description)) },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )

                        Divider()

                        TextInput(
                            input = viewModel.valueRangeTarget.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeTarget(it))
                            },
                            label = getString(MR.strings.value_range_target),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            suffix = { Text(viewState.unitName) },
                            supportingText = { Text(stringResource(MR.strings.value_range_target_description)) },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )

                        Divider()

                        TextInput(
                            input = viewModel.valueRangeHigh.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeHigh(it))
                            },
                            label = getString(MR.strings.value_range_high),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            suffix = { Text(viewState.unitName) },
                            supportingText = { Text(stringResource(MR.strings.value_range_high_description)) },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )

                        Divider()

                        TextInput(
                            input = viewModel.valueRangeMaximum.collectAsState().value,
                            onInputChange = {
                                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeMaximum(it))
                            },
                            label = getString(MR.strings.value_range_maximum),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            suffix = { Text(viewState.unitName) },
                            supportingText = { Text(stringResource(MR.strings.value_range_maximum_description)) },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done,
                            ),
                        )
                    }
                }
            }

            if (viewState.showDeletionDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dispatchIntent(MeasurementTypeFormIntent.HideDeletionDialog) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.dispatchIntent(MeasurementTypeFormIntent.DeleteType(viewState.type))
                        }) {
                            Text(getString( MR.strings.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            viewModel.dispatchIntent(MeasurementTypeFormIntent.HideDeletionDialog)
                        }) {
                            Text(getString( MR.strings.cancel))
                        }
                    },
                    title = { Text(getString(MR.strings.measurement_type_delete)) },
                    text = {
                        Text(
                            getString(
                                MR.strings.measurement_type_delete_description,
                                viewState.measurementCount,
                            )
                        )
                    },
                )
            }
        }

        is MeasurementTypeFormViewState.Error -> TODO()
    }
}