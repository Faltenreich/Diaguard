package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchField(
    query: String? = null,
    placeholder: String? = null,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = query ?: "",
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder?.let { { Text(placeholder) } },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = { ClearButton(onClick = { onValueChange("") }) },
        shape = RoundedCornerShape(40),
    )
}