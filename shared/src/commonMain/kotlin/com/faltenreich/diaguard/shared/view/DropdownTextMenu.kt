package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import com.faltenreich.diaguard.AppTheme

@Composable
fun DropdownTextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(
            x = AppTheme.dimensions.padding.P_3,
            y = -AppTheme.dimensions.padding.P_2,
        ),
        modifier = modifier,
    ) {
        items.forEach { (label, onClick) ->
            DropdownMenuItem(
                text = { Text(label) },
                onClick = {
                    onClick()
                    onDismissRequest()
                },
            )
        }
    }
}