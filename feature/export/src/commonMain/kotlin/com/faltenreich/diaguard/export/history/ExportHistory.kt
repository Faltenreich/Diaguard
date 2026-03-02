package com.faltenreich.diaguard.export.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.divider.Divider

@Composable
internal fun ExportHistory(
    state: ExportHistoryState?,
    onIntent: (ExportHistoryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    LazyColumn(modifier = modifier) {
        items(items = state.files) { file ->
            Column {
                ExportHistoryListItem(file, onIntent)
                Divider()
            }
        }
    }
}