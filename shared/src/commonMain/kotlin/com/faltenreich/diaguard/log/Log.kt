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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
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
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val state = viewModel.collectState() ?: return
    val paginationItems = viewModel.pagingData.collectAsPaginationItems()
    val listState = rememberLazyListState()

    // Compensate initial scroll offset for month header
    LaunchedEffect(state.monthHeaderSize.height) {
        listState.scrollBy(-state.monthHeaderSize.height.toFloat())
    }

    LaunchedEffect(state) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .filter { it.offset > state.monthHeaderSize.height }
                .takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { nextItems ->
            val firstItem = paginationItems.get(nextItems.first().index - 1) ?: return@collect
            viewModel.dispatchIntent(LogIntent.OnScroll(firstItem, nextItems))
        }
    }

    Box {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
        ) {
            for (index in 0 until paginationItems.itemCount) {
                when (val peek = paginationItems.peek(index)) {
                    is LogItem.MonthHeader -> stickyHeader(key = peek.key) {
                        val item = paginationItems.get(index) as? LogItem.MonthHeader
                        checkNotNull(item)
                        LogMonth(
                            item = item,
                            modifier = Modifier.onGloballyPositioned {
                                viewModel.dispatchIntent(LogIntent.CacheMonthHeaderSize(it.size))
                            },
                        )
                    }

                    is LogItem.EntryContent -> item(key = peek.key) {
                        val item = paginationItems.get(index) as? LogItem.EntryContent
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
                        val item = paginationItems.get(index) as? LogItem.EmptyContent
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

        state.stickyHeaderInfo.date?.let { date ->
            LogDay(
                date = date,
                style = state.stickyHeaderInfo.style,
                modifier = Modifier
                    .onGloballyPositioned {
                        viewModel.dispatchIntent(LogIntent.CacheDayHeaderSize(it.size))
                    }
                    .offset { state.stickyHeaderInfo.offset }
                    .drawWithContent {
                        clipRect(top = state.stickyHeaderInfo.clip) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(AppTheme.colors.scheme.surface)
                    .padding(all = AppTheme.dimensions.padding.P_3)
            )
        }
    }
}