package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitList
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeForm
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.values

@Composable
fun MeasurementTypeForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    // TODO: Simplify state handling
    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)

        is MeasurementTypeFormViewState.Loaded -> {
            Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                TextInput(
                    input = viewModel.typeName.collectAsState().value,
                    onInputChange = { viewModel.typeName.value = it },
                    label = getString(Res.string.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.dimensions.padding.P_3),
                )

                if (viewState.type.property.isUserGenerated) {
                    TextInput(
                        input = viewModel.unitName.collectAsState().value,
                        onInputChange = { viewModel.unitName.value = it },
                        label = getString(Res.string.measurement_unit),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimensions.padding.P_3),
                    )
                } else {
                    MeasurementUnitList(units = viewState.type.units)
                }

                TextDivider(getString(Res.string.values))

                MeasurementValueRangeForm(
                    viewState = viewState,
                    viewModel = viewModel,
                )
            }
        }

        is MeasurementTypeFormViewState.Error -> TODO()
    }
}