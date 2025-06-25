package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.ic_preferences
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        singleLine = true,
        shape = AppTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppTheme.colors.scheme.background,
            unfocusedContainerColor = AppTheme.colors.scheme.background,
            // Removing bottom border
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

@Preview
@Composable
private fun Preview() {
    SearchField(
        query = "Query",
        placeholder = "Placeholder",
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_preferences),
                contentDescription = null,
            )
        },
        onQueryChange = {},
    )
}