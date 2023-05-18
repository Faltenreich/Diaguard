package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementPropertyInput(
    property: MeasurementProperty,
    types: List<MeasurementType>,
    modifier: Modifier = Modifier,
    onInputChange: (String, MeasurementType) -> Unit,
) {
    FormRow(
        icon = { Text(property.icon ?: property.name.firstOrNull()?.toString() ?: "") },
        modifier = modifier,
    ) {
        Column {
            types.forEach { type ->
                MeasurementTypeInput(
                    type = type,
                    modifier = Modifier.fillMaxWidth(),
                    onInputChange = onInputChange,
                )
            }
        }
    }
}