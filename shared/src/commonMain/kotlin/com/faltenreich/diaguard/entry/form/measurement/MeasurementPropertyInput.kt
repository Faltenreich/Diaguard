package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementPropertyInput(
    state: MeasurementPropertyInputState,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (state.isLast) ImeAction.Done else ImeAction.Next,
        ),
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementPropertyInput(
        state = MeasurementPropertyInputState(
            property = property(),
            input = "",
            isLast = true,
            error = null,
            decimalPlaces = 3,
        ),
        onIntent = {},
    )
}