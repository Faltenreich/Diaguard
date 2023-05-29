package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.pagination.onPagination
import com.faltenreich.diaguard.shared.view.SwipeToDismiss
import com.faltenreich.diaguard.shared.view.rememberSwipeToDismissState
import kotlinx.coroutines.launch

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    val coroutineScope = rememberCoroutineScope()
    // FIXME: Gets not updated on entry change
    val viewState = viewModel.viewState.collectAsState().value
    val listState = rememberLazyListState().onPagination(
        buffer = 10,
        condition = viewState.items.isNotEmpty(),
        onPagination = viewModel::onPagination,
    )
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
        viewState.items.forEach { (monthOfYear, items) ->
            stickyHeader { LogMonth(monthOfYear) }
            items(items, key = { it.key }) {item ->
                when (item) {
                    is LogItem.MonthHeader -> Unit
                    is LogItem.DayHeader -> LogDay(item.date)
                    is LogItem.EntryContent -> {
                        val swipeToDismissState = rememberSwipeToDismissState()
                        // TODO: Animate item replacement
                        if (swipeToDismissState.isDismissed()) {
                            viewModel.remove(item)
                        }
                        SwipeToDismiss(
                            state = swipeToDismissState,
                            background = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(AppTheme.colorScheme.error),
                                )
                            },
                        ) {
                            LogEntry(
                                entry = item.entry,
                            )
                        }
                    }
                    is LogItem.EmptyContent -> LogEmpty()
                }
            }
        }
    }
}
