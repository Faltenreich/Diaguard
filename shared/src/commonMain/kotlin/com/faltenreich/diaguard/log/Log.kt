package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Log(viewModel: LogViewModel = inject()) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is LogViewState.Requesting -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }
        is LogViewState.Responding -> EntryList(viewState.entries)
    }
}