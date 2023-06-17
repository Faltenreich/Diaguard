package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementPropertyInput(
    viewState: MeasurementPropertyInputViewState,
    modifier: Modifier = Modifier,
    onInputChange: (MeasurementTypeInputViewState) -> Unit,
) {
    FormRow(
        icon = { MeasurementPropertyIcon(viewState.property) },
        modifier = modifier,
    ) {
        Column {
            viewState.values.forEach { value ->
                MeasurementTypeInput(
                    viewState = value,
                    onInputChange = onInputChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}