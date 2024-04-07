package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme

@Composable
fun SearchField(
    query: String = "",
    placeholder: String?,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder?.let { { Text(placeholder) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = AppTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppTheme.colors.scheme.surfaceContainer,
            unfocusedContainerColor = AppTheme.colors.scheme.surfaceContainer,
            // Removing bottom border
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}