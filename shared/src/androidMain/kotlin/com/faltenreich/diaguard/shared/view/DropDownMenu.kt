package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset

@Composable
actual fun DropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    offset: DpOffset,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded,
        onDismissRequest,
        modifier,
        offset,
        // PopupProperties are not platform-agnostic
        content = content,
    )
}