package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DropdownMenuItem
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
    DropdownMenuItem(
        text,
        onClick,
        modifier,
        leadingIcon,
        trailingIcon,
        enabled,
    )
}