package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SearchField(
    query: String = "",
    placeholder: String?,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder?.let { { Text(placeholder) } },
        leadingIcon = {
            Icon(
                painter = painterResource(MR.images.ic_search),
                contentDescription = null,
            )
        },
        trailingIcon = { ClearButton(onClick = { onQueryChange("") }) },
        shape = AppTheme.shapes.extraLarge,
        colors = TextFieldDefaults.colors(
            // Removing bottom border
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}