package com.faltenreich.rhyme.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun DropDownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    enabled: Boolean,
) {
    TODO()
}