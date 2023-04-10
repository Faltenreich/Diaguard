package com.faltenreich.rhyme.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DropDownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
)