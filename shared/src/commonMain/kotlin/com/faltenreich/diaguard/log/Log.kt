package com.faltenreich.diaguard.log

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.log.list.LogList
import com.faltenreich.diaguard.log.list.item.LogDaySticky
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.shared.view.LifecycleState
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.shared.view.rememberLifecycleState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Log(
    state: LogState?,
    onIntent: (LogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    val listState = rememberLazyListState()
    val items = state.pagingData.collectAsLazyPagingItems()

    val monthHeaderHeight = with(LocalDensity.current) { AppTheme.dimensions.size.LogMonthHeight.roundToPx() }

    LaunchedEffect(monthHeaderHeight) {
        // Avoid scrolling on resume
        if (listState.firstVisibleItemScrollOffset == 0) {
            listState.scrollBy(-monthHeaderHeight.toFloat())
        }
    }

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            // FIXME: Jumps to start of page
            items.refresh()
        }
    }

    LaunchedEffect(state, onIntent) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .filter { it.offset > monthHeaderHeight }
                .takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { nextItems ->
            val firstItem = items[nextItems.first().index - 1] ?: return@collect
            onIntent(LogIntent.OnScroll(firstItem, nextItems, monthHeaderHeight))
        }
    }

    Box(modifier = modifier) {
        LogList(
            state = listState,
            items = items,
            onIntent = onIntent,
            modifier = Modifier.fillMaxSize(),
        )
        LogDaySticky(
            state = state,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    onIntent(LogIntent.CacheDayHeaderSize(coordinates.size))
                },
        )
    }

    state.datePickerDialog?.let { datePickerDialog ->
        DatePickerDialog(
            date = datePickerDialog.date,
            onDismissRequest = { onIntent(LogIntent.CloseDatePickerDialog) },
            onConfirmRequest = { date ->
                onIntent(LogIntent.CloseDatePickerDialog)
                onIntent(LogIntent.SetDate(date))
            },
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    Log(
        state = LogState(
            pagingData = flowOf(PagingData.from(emptyList())),
            dayHeaderSize = IntSize.Zero,
            dayStickyInfo = LogDayStickyInfo(),
            datePickerDialog = null,
        ),
        onIntent = {},
    )
}