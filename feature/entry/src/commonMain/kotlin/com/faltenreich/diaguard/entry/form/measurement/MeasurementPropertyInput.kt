package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.view.input.TextInput

@Composable
fun MeasurementPropertyInput(
    state: MeasurementPropertyInputState,
    keyboardOptions: KeyboardOptions,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var input by remember { mutableStateOf(state.input) }

    TextInput(
        input = input,
        onInputChange = {
            input = it
            onIntent(EntryFormIntent.Edit(state.copy(input = input)))
        },
        modifier = modifier,
        placeholder = { Text(state.property.unit.abbreviation) },
        suffix = {
            if (state.property.name != state.property.category.name) {
                Text(state.property.name)
            }
        },
        maxLines = 1,
        keyboardOptions = keyboardOptions,
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    MeasurementPropertyInput(
        state = MeasurementPropertyInputState(
            property = property(),
            input = "",
            isLast = true,
            error = null,
            decimalPlaces = 3,
        ),
        keyboardOptions = KeyboardOptions.Default,
        onIntent = {},
    )
}