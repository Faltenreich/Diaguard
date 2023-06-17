package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementTypeInput(
    data: MeasurementTypeInputData,
    onInputChange: (MeasurementTypeInputData) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = data.input,
        onInputChange = { input -> onInputChange(data.copy(input = input)) },
        hint = data.type.name,
        modifier = modifier,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}