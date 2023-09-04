package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow

    when (val viewState = viewModel.viewState.collectAsState().value) {
        is MeasurementTypeFormViewState.Loading -> LoadingIndicator()

        is MeasurementTypeFormViewState.Loaded -> {
            LazyColumn(modifier = modifier) {
                item {
                    TextInput(
                        input = viewModel.typeName.collectAsState().value,
                        onInputChange = { input -> viewModel.typeName.value = input },
                        label = stringResource(MR.strings.name),
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
                            label = stringResource(MR.strings.measurement_unit),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.dimensions.padding.P_3),
                        )
                    }
                } else {
                    MeasurementUnitList(units = viewState.type.units)
                }
            }

            if (viewState.showDeletionDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::hideDeletionDialog,
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteType(viewState.type)
                            viewModel.hideDeletionDialog()
                            navigator.pop()
                        }) {
                            Text(stringResource( MR.strings.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDeletionDialog) {
                            Text(stringResource( MR.strings.cancel))
                        }
                    },
                    title = { Text(stringResource(MR.strings.measurement_type_delete)) },
                    text = { Text(stringResource(MR.strings.measurement_type_delete_description, viewState.measurementCount)) },
                )
            }
        }

        is MeasurementTypeFormViewState.Error -> Unit
    }
}