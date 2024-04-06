package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    val state = viewModel.collectState()
    val types = state?.types

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
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

        AnimatedVisibility(
            visible = types != null,
            enter = fadeIn(),
        ) {
            MeasurementTypeList(
                property = viewModel.property,
                types = types ?: emptyList(),
            )
        }
    }
}