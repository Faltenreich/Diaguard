package com.faltenreich.diaguard.log

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryList

@Composable
fun Log(
    viewModel: LogViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.viewState.collectAsState().value
    EntryList(
        entries = state.entries,
        modifier = modifier,
        onDelete = viewModel::delete,
    )
}