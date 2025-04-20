package com.faltenreich.diaguard.log

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePicker
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItemState
import com.faltenreich.diaguard.log.item.LogLoadingIndicator
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.view.LifecycleState
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.rememberLifecycleState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun Log(
    viewModel: LogViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    val items = viewModel.pagingData.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    LaunchedEffect(state.monthHeaderSize.height) {
        // Avoid scrolling on resume
        if (listState.firstVisibleItemScrollOffset == 0) {
            listState.scrollBy(-state.monthHeaderSize.height.toFloat())
        }
    }

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            items.refresh()
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (state.monthHeaderSize != IntSize.Zero) 1f else 0f,
        animationSpec = tween(),
    )

    LaunchedEffect(state) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .filter { it.offset > state.monthHeaderSize.height }
                .takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { nextItems ->
            val firstItem = items[nextItems.first().index - 1] ?: return@collect
            viewModel.dispatchIntent(LogIntent.OnScroll(firstItem, nextItems))
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha),
            state = listState,
        ) {
            if (items.loadState.prepend == LoadState.Loading) {
                item {
                    LogLoadingIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

            for (index in 0 until items.itemCount) {
                when (val peek = items.peek(index)) {
                    is LogItemState.MonthHeader -> stickyHeader(key = peek.key) {
                        val item = items[index] as? LogItemState.MonthHeader
                        checkNotNull(item)
                        LogMonth(
                            state = item,
                            modifier = Modifier.onGloballyPositioned {
                                viewModel.dispatchIntent(LogIntent.CacheMonthHeaderSize(it.size))
                            },
                        )
                    }

                    is LogItemState.EntryContent -> item(key = peek.key) {
                        val item = items[index] as? LogItemState.EntryContent
                        checkNotNull(item)
                        LogEntry(
                            state = item,
                            onClick = { viewModel.dispatchIntent(LogIntent.OpenEntry(item.entryState.entry)) },
                            onTagClick = { tag ->
                                viewModel.dispatchIntent(LogIntent.OpenEntrySearch(query = tag.name))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = AppTheme.dimensions.padding.P_3,
                                    vertical = AppTheme.dimensions.padding.P_2,
                                ),
                        )
                    }

                    is LogItemState.EmptyContent -> item(key = peek.key) {
                        val item = items[index] as? LogItemState.EmptyContent
                        checkNotNull(item)
                        LogEmpty(
                            state = item,
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

            if (items.loadState.append == LoadState.Loading) {
                item {
                    LogLoadingIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }

        val stickyDate = state.stickyHeaderInfo.date
        if (stickyDate != null) {
            LogDay(
                date = stickyDate,
                style = state.stickyHeaderInfo.style,
                modifier = Modifier
                    .onGloballyPositioned {
                        viewModel.dispatchIntent(LogIntent.CacheDayHeaderSize(it.size))
                    }
                    .alpha(alpha)
                    .offset { state.stickyHeaderInfo.offset }
                    .drawWithContent {
                        clipRect(top = state.stickyHeaderInfo.clip) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(AppTheme.colors.scheme.background)
                    .padding(all = AppTheme.dimensions.padding.P_3)
            )
        }
    }

    state.dateDialog?.let { dateDialog ->
        DatePicker(
            date = dateDialog.date,
            onPick = { date ->
                viewModel.dispatchIntent(LogIntent.CloseDateDialog)
                viewModel.dispatchIntent(LogIntent.SetDate(date))
            },
        )
    }
}