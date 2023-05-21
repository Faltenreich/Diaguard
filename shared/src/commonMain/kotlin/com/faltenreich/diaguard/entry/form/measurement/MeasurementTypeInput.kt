package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementTypeInput(
    value: MeasurementInputViewState.Property.Value,
    onInputChange: (String, MeasurementType) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = value.input,
        hint = value.type.name,
        modifier = modifier,
        onInputChange = { input -> onInputChange(input, value.type) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}