package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.faltenreich.diaguard.view.input.TextInput
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.data.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.tag
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryTagInput(
    input: String,
    onInputChange: (String) -> Unit,
    suggestions: Collection<Tag>,
    onSuggestionSelect: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier,
    ) {
        TextInput(
            input = input,
            onInputChange = onInputChange,
            placeholder = { Text(stringResource(Res.string.tag)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (input.isNotBlank()) {
                        onSuggestionSelect(Tag.User(name = input))
                    }
                },
            ),
        )

        if (suggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion.name) },
                        onClick = {
                            onSuggestionSelect(suggestion)
                            isExpanded = false
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    EntryTagInput(
        input = "Input",
        onInputChange = {},
        suggestions = emptyList(),
        onSuggestionSelect = {},
    )
}