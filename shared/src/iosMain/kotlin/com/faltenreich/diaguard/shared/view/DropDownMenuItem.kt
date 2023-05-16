package com.faltenreich.diaguard.shared.view

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
    TODO("Not yet implemented")
}