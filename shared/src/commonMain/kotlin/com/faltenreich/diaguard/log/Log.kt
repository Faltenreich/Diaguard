package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryList

@Composable
fun Log(
    viewModel: LogViewModel,
    modifier: Modifier = Modifier,
) {
    when (val state = viewModel.viewState.collectAsState().value) {
        is LogViewState.Requesting -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }
        is LogViewState.Responding -> EntryList(
            entries = state.entries,
            modifier = modifier,
            onDelete = viewModel::delete,
        )
    }
}