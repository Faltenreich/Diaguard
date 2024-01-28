package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val items = viewModel.state.collectAsPaginationItems()
    val listState = rememberLazyListState()
    var monthHeaderHeightPx by remember { mutableStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { firstVisibleItemIndex ->
                // FIXME: Called when empty
                if (items.itemCount > 0) {
                    // FIXME: Use monthHeaderHeightPx to return first item that is not hidden by the month header
                    items.get(firstVisibleItemIndex)?.date?.let { firstVisibleDate ->
                        viewModel.currentDate.value = firstVisibleDate
                    }
                }
            }
    }

    Box {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
        ) {
            for (index in 0 until items.itemCount) {
                when (val peek = items.peek(index)) {
                    is LogItem.MonthHeader -> stickyHeader(key = peek.key) {
                        val item = items.get(index) as? LogItem.MonthHeader
                        checkNotNull(item)
                        LogMonth(
                            item = item,
                            modifier = Modifier.onGloballyPositioned { monthHeaderHeightPx = it.size.height },
                        )
                    }
                    is LogItem.EntryContent -> item(key = peek.key) {
                        val item = items.get(index) as? LogItem.EntryContent
                        checkNotNull(item)
                        // TODO: Animate item replacement
                        SwipeToDismissBox(
                            state = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    viewModel.dispatchIntent(LogIntent.Remove(item))
                                    true
                                }
                            ),
                            backgroundContent = { Box(modifier = Modifier.fillMaxSize()) },
                        ) {
                            LogEntry(
                                item = item,
                                onClick = { viewModel.dispatchIntent(LogIntent.OpenEntry(item.entry)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = AppTheme.dimensions.padding.P_3,
                                        vertical = AppTheme.dimensions.padding.P_2,
                                    ),
                            )
                        }
                    }
                    is LogItem.EmptyContent -> item(key = peek.key) {
                        val item = items.get(index) as? LogItem.EmptyContent
                        checkNotNull(item)
                        LogEmpty(
                            item = item,
                            onIntent = viewModel::dispatchIntent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                        )
                    }
                    null -> item {
                        Skeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.size.TouchSizeMedium),
                        )
                    }
                }
            }
        }
    }
}
