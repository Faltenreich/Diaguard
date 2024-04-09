package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputState,
    action: @Composable (() -> Unit)? = null,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = data.input,
        onInputChange = { input ->
            onIntent(EntryFormIntent.Edit(data.copy(input = input)))
        },
        modifier = modifier,
        label = data.property.name,
        trailingIcon = action,
        suffix = { Text(data.property.selectedUnit.abbreviation) },
        supportingText = data.error?.let { error -> { Text(error) } },
        isError = data.error != null,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (data.isLast) ImeAction.Done else ImeAction.Next,
        ),
    )
}