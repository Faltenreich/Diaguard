package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.SwipeToDismiss
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import com.faltenreich.diaguard.shared.view.rememberSwipeToDismissState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val items = viewModel.items.collectAsPaginationItems()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { firstVisibleItemIndex ->
                val firstVisibleDate = try {
                    items.get(firstVisibleItemIndex)?.date
                } catch (exception: Exception) {
                    null
                } ?: return@collect
                viewModel.currentDate.value = firstVisibleDate
            }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
    ) {
        for (index in 0 until items.itemCount) {
            when (val peek = items.peek(index)) {
                is LogItem.MonthHeader -> stickyHeader(key = peek.key) {
                    val item = items.get(index) ?: throw IllegalStateException()
                    LogMonth(item.date.monthOfYear)
                }
                is LogItem.DayHeader -> item(key = peek.key) {
                    val item = items.get(index) ?: throw IllegalStateException()
                    LogDay(item.date,)
                }
                is LogItem.EntryContent -> item(key = peek.key) {
                    val item = items.get(index) as? LogItem.EntryContent ?: throw IllegalStateException()
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
                                    .fillMaxSize(),
                            )
                        },
                    ) {
                        LogEntry(
                            entry = item.entry,
                        )
                    }
                }
                is LogItem.EmptyContent -> item(key = peek.key) {
                    val item = items.get(index) ?: throw IllegalStateException()
                    LogEmpty(item.date)
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
