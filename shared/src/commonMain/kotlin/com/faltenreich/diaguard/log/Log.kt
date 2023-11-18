package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
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
    val density = LocalDensity.current
    // FIXME: Gets not updated on entry change
    val items = viewModel.items.collectAsPaginationItems()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { firstVisibleItemIndex ->
                val firstVisibleDate = items.get(firstVisibleItemIndex)?.date
                viewModel.currentDate.value = checkNotNull(firstVisibleDate)
            }
    }

    var monthHeaderHeightPx by remember { mutableStateOf(0) }
    var dayHeaderWidthDp by remember { mutableStateOf(0.dp) }

    Box {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
        ) {
            for (index in 0 until items.itemCount) {
                when (val peek = items.peek(index)) {
                    is LogItem.MonthHeader -> stickyHeader(key = peek.key) {
                        val item = items.get(index)
                        checkNotNull(item)
                        LogMonth(
                            monthOfYear = item.date.monthOfYear,
                            modifier = Modifier.onGloballyPositioned { monthHeaderHeightPx = it.size.height },
                        )
                    }
                    is LogItem.DayHeader -> item(key = peek.key) {
                        val item = items.get(index)
                        checkNotNull(item)
                        LogDay(item.date)
                    }
                    is LogItem.EntryContent -> item(key = peek.key) {
                        val item = items.get(index) as? LogItem.EntryContent
                        checkNotNull(item)
                        // TODO: Animate item replacement
                        val swipeToDismissState = rememberDismissState(
                            confirmValueChange = {
                                viewModel.remove(item)
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = swipeToDismissState,
                            background = { Box(modifier = Modifier.fillMaxSize()) },
                            dismissContent = {
                                LogEntry(
                                    entry = item.entry,
                                    modifier = Modifier.padding(start = dayHeaderWidthDp),
                                )
                            },
                        )
                    }
                    is LogItem.EmptyContent -> item(key = peek.key) {
                        val item = items.get(index)
                        checkNotNull(item)
                        LogEmpty(
                            date = item.date,
                            modifier = Modifier.padding(start = dayHeaderWidthDp),
                        )
                    }
                    null -> item {
                        Skeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.size.TouchSizeMedium)
                                .padding(start = dayHeaderWidthDp),
                        )
                    }
                }
            }
        }
        // TODO: Move behind LazyColumn if complete
        LogDay(
            date = viewModel.currentDate.value,
            modifier = Modifier
                .offset {
                    // FIXME: Calculate correct offset
                    val firstVisibleDay = listState.layoutInfo.visibleItemsInfo.firstOrNull {
                        (it.key as? String)?.startsWith("Day") == true
                    }
                    val firstVisibleDayOffset = firstVisibleDay?.offset ?: 0
                    val offsetY = monthHeaderHeightPx - firstVisibleDayOffset
                    IntOffset(0, offsetY)
                }
                .onGloballyPositioned { dayHeaderWidthDp = with(density) { it.size.width.toDp() } },
        )
    }
}
