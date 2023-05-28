package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.log.item.LogData
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.pagination.onPagination
import kotlinx.coroutines.launch

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    val coroutineScope = rememberCoroutineScope()
    val viewState = viewModel.viewState.collectAsState().value
    val listState = rememberLazyListState().onPagination(buffer = 10, viewModel::onPagination)
    viewModel.onScroll(listState.firstVisibleItemIndex)
    viewState.scrollPosition?.let { scrollPosition ->
        coroutineScope.launch {
            listState.scrollToItem(scrollPosition)
            viewModel.resetScroll()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
    ) {
        viewState.data.forEach { (month, data) ->
            stickyHeader { LogMonth(month) }
            items(data, key = { it.key }) {data ->
                when (data) {
                    is LogData.MonthHeader -> Unit
                    is LogData.DayHeader -> LogDay(data.date)
                    is LogData.EntryContent -> LogEntry(data.entry, onDelete = viewModel::delete)
                    is LogData.EmptyContent -> LogEmpty()
                }
            }
        }
    }
}
