package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)

        is MeasurementPropertyFormViewState.Loaded -> {
            Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                TextInput(
                    input = viewModel.name.collectAsState().value,
                    onInputChange = { input -> viewModel.name.value = input },
                    label = getString(Res.string.name),
                    leadingIcon = {
                        IconButton(onClick = {
                            viewModel.dispatchIntent(MeasurementPropertyFormIntent.OpenIconPicker)
                        }) {
                            MeasurementPropertyIcon(text = viewModel.icon.collectAsState().value)
                        }
                    },
                    modifier = Modifier
                        .padding(all = AppTheme.dimensions.padding.P_3)
                        .fillMaxWidth(),
                )
                MeasurementTypeList(
                    property = viewModel.property,
                    types = viewState.types,
                )
            }
        }
    }
}