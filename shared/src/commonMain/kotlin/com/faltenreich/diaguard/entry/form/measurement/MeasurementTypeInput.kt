package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementTypeInput(
    type: MeasurementType,
    onInputChange: (String, MeasurementType) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = "",
        hint = type.name,
        modifier = modifier,
        onInputChange = { input -> onInputChange(input, type) },
    )
}