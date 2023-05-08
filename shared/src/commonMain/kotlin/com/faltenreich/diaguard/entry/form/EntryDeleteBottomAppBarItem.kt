package com.faltenreich.diaguard.entry.form

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem

@Composable
fun EntrySearchBottomAppBarItem(
    onClick: () -> Unit,
) {
    BottomAppBarItem(
        image = Icons.Filled.Delete,
        contentDescription = MR.strings.entry_delete,
        onClick = onClick,
    )
}