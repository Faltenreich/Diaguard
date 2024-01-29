package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.min

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val items = viewModel.state.collectAsPaginationItems()
    val listState = rememberLazyListState()
    var monthHeaderHeightPx by remember { mutableStateOf(0) }

    // Compensate initial scroll offset for month header
    LaunchedEffect(monthHeaderHeightPx) {
        listState.scrollBy(-monthHeaderHeightPx.toFloat())
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.firstOrNull { info ->
                // FIXME: Returns next day when scrolling through subsequent entries of day
                info.offset >= monthHeaderHeightPx
            }
        }
            .distinctUntilChanged()
            .collect { firstVisibleItem ->
                if (firstVisibleItem != null) {
                    items.get(firstVisibleItem.index)?.date?.let { firstVisibleDate ->
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

        val stickyDate = viewModel.currentDate.collectAsState().value.minus(1, DateUnit.DAY)
        var dayItemHeightPx by remember { mutableStateOf(0) }
        LogDay(
            date = stickyDate,
            style = LogDayStyle.NORMAL,
            modifier = Modifier
                .onGloballyPositioned { dayItemHeightPx = it.size.height }
                .offset {
                    if (listState.layoutInfo.totalItemsCount < 2) return@offset IntOffset.Zero
                    val dateAfterStickyDate = listState.layoutInfo.visibleItemsInfo
                        .first { info ->
                            val item = info.key as? LogKey.Item ?: return@first false
                            info.offset >= monthHeaderHeightPx && item.isFirstOfDay && item.date.isAfter(stickyDate)
                        }
                    val yOffset = min(monthHeaderHeightPx, dateAfterStickyDate.offset - dayItemHeightPx)
                    IntOffset(0, yOffset)
                }
                .background(Color.Red)
                .padding(all = AppTheme.dimensions.padding.P_3)
        )
    }
}
