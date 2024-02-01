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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.min

// TODO: Improve performance including not initial jump for scroll offset
@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val items = viewModel.state.collectAsPaginationItems()
    val stickyDate = viewModel.currentDate.collectAsState().value
    val listState = rememberLazyListState()

    var monthHeight by remember { mutableStateOf(0) }
    var dayHeight by remember { mutableStateOf(0) }
    var stickyDayOffset by remember { mutableStateOf(IntOffset.Zero) }
    var stickyDayTopClip by remember { mutableStateOf(0f) }

    // Compensate initial scroll offset for month header
    LaunchedEffect(monthHeight) {
        listState.scrollBy(-monthHeight.toFloat())
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .filter { it.offset > monthHeight }
                .takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { nextItems ->
            val firstItem = items.get(nextItems.first().index - 1)
                ?: return@collect

            viewModel.currentDate.value = firstItem.date

            if (firstItem is LogItem.MonthHeader) {
                stickyDayOffset = IntOffset(0, -dayHeight)
                return@collect
            }

            val nextItem = nextItems.firstOrNull { item ->
                when (val key = item.key) {
                    is LogKey.Header -> true
                    is LogKey.Item -> key.isFirstOfDay && key.date > firstItem.date
                    else -> true
                }
            }
            val yOffset = when (nextItem?.key) {
                is LogKey.Header -> -dayHeight
                is LogKey.Item -> min(monthHeight, nextItem.offset - dayHeight)
                else -> -dayHeight
            }
            stickyDayOffset = IntOffset(0, yOffset)

            val overlap = -(yOffset - monthHeight)
            stickyDayTopClip = if (overlap > 0) overlap.toFloat() else 0f
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
                            modifier = Modifier.onGloballyPositioned {
                                monthHeight = it.size.height
                            },
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

        // Sticky header
        LogDay(
            date = stickyDate,
            style = LogDayStyle.NORMAL, // TODO
            modifier = Modifier
                .onGloballyPositioned { dayHeight = it.size.height }
                .offset { stickyDayOffset }
                .drawWithContent { clipRect(top = stickyDayTopClip) { this@drawWithContent.drawContent() } }
                .background(AppTheme.colors.scheme.surface)
                .padding(all = AppTheme.dimensions.padding.P_3)
        )
    }
}
