package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextInput(
    input: String,
    hint: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = input,
        onValueChange = onInputChange,
        modifier = modifier,
        placeholder = { Text(hint) },
    )
}