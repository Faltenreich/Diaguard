package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementPropertyInput(
    property: MeasurementInputViewState.Property,
    modifier: Modifier = Modifier,
    onInputChange: (MeasurementType, String) -> Unit,
) {
    FormRow(
        icon = { Text(property.property.icon ?: property.property.name.firstOrNull()?.toString() ?: "") },
        modifier = modifier,
    ) {
        Column {
            property.values.forEach { value ->
                MeasurementTypeInput(
                    value = value,
                    onInputChange = { input -> onInputChange(value.type, input) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}