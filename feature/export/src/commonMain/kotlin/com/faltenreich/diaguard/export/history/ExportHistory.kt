package com.faltenreich.diaguard.export.history

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ExportHistory(
    state: ExportHistoryState?,
    onIntent: (ExportHistoryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    LazyColumn(modifier = modifier) {
        items(items = state.files) { file ->
            Text(file.dateTime.toString())
        }
    }
}