package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
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

    var dayHeaderHeight by remember { mutableStateOf(0) }

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            // FIXME: Jumps to start of page
            items.refresh()
        }
    }

    LaunchedEffect(state, onIntent) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { visibleItems ->
            val firstItem = items[visibleItems.first().index] ?: return@collect
            val nextItems = visibleItems.takeLast(visibleItems.size - 1)
            onIntent(LogIntent.OnScroll(firstItem, nextItems, dayHeaderHeight))
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
            modifier = Modifier.onGloballyPositioned { coordinates ->
                dayHeaderHeight = coordinates.size.height
            }
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
            initialDate = today(),
            pagingData = flowOf(PagingData.from(emptyList())),
            dayStickyInfo = LogDayStickyInfo(),
            datePickerDialog = null,
        ),
        onIntent = {},
    )
}