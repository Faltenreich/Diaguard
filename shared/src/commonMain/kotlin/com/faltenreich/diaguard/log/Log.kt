package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is LogViewState.Requesting -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }
        is LogViewState.Responding -> LazyColumn {
            items(items = viewState.data, key = LogData::key) { data ->
                when (data) {
                    is LogData.MonthHeader -> LogMonth(data.date)
                    is LogData.DayHeader -> LogDay(data.date)
                    is LogData.EntryContent -> LogEntry(data.entry, onDelete = viewModel::delete)
                    is LogData.EmptyContent -> LogEmpty()
                }
            }
        }
    }
}