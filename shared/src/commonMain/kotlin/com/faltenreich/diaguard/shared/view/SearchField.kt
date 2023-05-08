package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SearchField(
    query: String? = null,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
) {
    TextField(
        value = query ?: "",
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder?.let { { Text(placeholder) } },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = { ClearButton(onClick = { onQueryChange("") }) },
        shape = RoundedCornerShape(40),
        colors = TextFieldDefaults.textFieldColors(
            // Removing bottom border
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}