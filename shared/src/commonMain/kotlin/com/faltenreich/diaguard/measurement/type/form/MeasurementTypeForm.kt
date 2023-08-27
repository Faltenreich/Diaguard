package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.FormRowLabel
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementTypeFormViewState.Loading -> Unit

        is MeasurementTypeFormViewState.Loaded -> Column(modifier = modifier) {
            TextInput(
                input = viewModel.name.collectAsState().value,
                onInputChange = { input -> viewModel.name.value = input },
                label = stringResource(MR.strings.name),
                modifier = Modifier.fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_3),
            )

            FormRowLabel(stringResource(MR.strings.measurement_units))

            LazyColumn {
                items(
                    items = state.typeUnits,
                    key = MeasurementTypeUnit::id,
                ) { typeUnit ->
                    MeasurementTypeUnitListItem(
                        typeUnit = typeUnit,
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable { viewModel.setSelectedTypeUnit(typeUnit) },
                    )
                }
            }
        }

        is MeasurementTypeFormViewState.Error -> Unit
    }
}