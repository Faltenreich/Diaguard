package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.SwipeToDismiss
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import com.faltenreich.diaguard.shared.view.items
import com.faltenreich.diaguard.shared.view.rememberSwipeToDismissState

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val viewState = viewModel.viewState.collectAsState().value
    // FIXME: Previous items are added on top which causes pagination loop
    val items = viewModel.items.collectAsPaginationItems()
    // TODO: viewModel.onScroll(listState.firstVisibleItemIndex)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(items, key = { it.key }) { item ->
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
                null -> Skeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.size.MediumTouchSize),
                )
            }
        }
    }
}
