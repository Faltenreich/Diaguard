package com.faltenreich.diaguard.log

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.log.list.LogList
import com.faltenreich.diaguard.log.list.item.LogDaySticky
import com.faltenreich.diaguard.shared.view.LifecycleState
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
        LogList(
            state = listState,
            items = items,
            onIntent = viewModel::dispatchIntent,
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha),
        )
        LogDaySticky(
            state = state,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    viewModel.dispatchIntent(LogIntent.CacheDayHeaderSize(coordinates.size))
                }
                .alpha(alpha),
        )
    }

    state.dateDialog?.let { dateDialog ->
        DatePickerDialog(
            date = dateDialog.date,
            onDismissRequest = { viewModel.dispatchIntent(LogIntent.CloseDateDialog) },
            onConfirmRequest = { date ->
                viewModel.dispatchIntent(LogIntent.CloseDateDialog)
                viewModel.dispatchIntent(LogIntent.SetDate(date))
            },
        )
    }
}