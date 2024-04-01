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
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput

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
                        onInputChange = { input -> viewModel.typeName.value = input },
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
                            onInputChange = { input -> viewModel.unitName.value = input },
                            label = getString(MR.strings.measurement_unit),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.dimensions.padding.P_3),
                        )
                    }
                } else {
                    MeasurementUnitList(units = viewState.type.units)
                }

                item {
                    Column {
                        TextDivider(getString(MR.strings.values))
                        TextInput(
                            input = viewModel.minimumValue.collectAsState().value,
                            onInputChange = { input -> viewModel.minimumValue.value = input },
                            label = getString(MR.strings.value_minimum),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )
                        Divider()
                        TextInput(
                            input = viewModel.lowValue.collectAsState().value,
                            onInputChange = { input -> viewModel.lowValue.value = input },
                            label = getString(MR.strings.value_low),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )
                        Divider()
                        TextInput(
                            input = viewModel.targetValue.collectAsState().value,
                            onInputChange = { input -> viewModel.targetValue.value = input },
                            label = getString(MR.strings.value_target),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )
                        Divider()
                        TextInput(
                            input = viewModel.highValue.collectAsState().value,
                            onInputChange = { input -> viewModel.highValue.value = input },
                            label = getString(MR.strings.value_high),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next,
                            ),
                        )
                        Divider()
                        TextInput(
                            input = viewModel.maximumValue.collectAsState().value,
                            onInputChange = { input -> viewModel.maximumValue.value = input },
                            label = getString(MR.strings.value_maximum),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
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