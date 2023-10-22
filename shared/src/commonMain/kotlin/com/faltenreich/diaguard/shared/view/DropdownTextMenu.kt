package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropdownTextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<DropdownTextMenuItem>,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        items.forEach { item ->
            DropdownTextMenuItem(
                onClick = {
                    item.onClick()
                    onDismissRequest()
                },
                text = item.label,
                isSelected = item.isSelected,
            )
        }
    }
}