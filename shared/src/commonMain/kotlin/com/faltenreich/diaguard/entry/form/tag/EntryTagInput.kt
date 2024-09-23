package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.tag

@Composable
fun EntryTagInput(
    input: String,
    onInputChange: (String) -> Unit,
    suggestions: List<Tag>,
    onSuggestionSelected: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
    ) {
        TextInput(
            input = input,
            onInputChange = onInputChange,
            label = getString(Res.string.tag),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion.name) },
                    onClick = {
                        onSuggestionSelected(suggestion)
                        isExpanded = false
                    },
                )
            }
        }
    }
}