package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
){
    Column(
        modifier = modifier.padding(all = AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        TextInput(
            input = viewModel.name,
            onInputChange = { input -> viewModel.name = input },
            label = stringResource(MR.strings.name),
            modifier = Modifier.fillMaxWidth(),
        )
        TextInput(
            input = viewModel.icon,
            onInputChange = { input -> viewModel.icon = input },
            label = stringResource(MR.strings.icon),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}