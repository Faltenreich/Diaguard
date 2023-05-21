package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementTypeInput(
    value: MeasurementInputViewState.Property.Value,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = value.input,
        onInputChange = onInputChange,
        hint = value.type.name,
        modifier = modifier,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}