package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementInput(
    property: MeasurementProperty,
    types: List<MeasurementType>,
    modifier: Modifier = Modifier,
    onInputChange: (String, MeasurementType) -> Unit,
) {
    Row(modifier = modifier) {
        Text(property.name)
        Column {
            types.forEach { type ->
                TextInput(
                    input = "",
                    hint = type.name,
                    onInputChange = { input -> onInputChange(input, type) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}