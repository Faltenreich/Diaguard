package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.log.list.LogList
import com.faltenreich.diaguard.log.list.item.LogDay
import com.faltenreich.diaguard.log.list.item.LogDayState
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.log.list.item.LogDayStyle
import com.faltenreich.diaguard.view.LifecycleState
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.view.rememberLifecycleState
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

    LaunchedEffect(
        listState.firstVisibleItemScrollOffset,
        listState.layoutInfo.visibleItemsInfo,
        dayHeaderHeight,
    ) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
            .takeIf(List<*>::isNotEmpty)
            ?: return@LaunchedEffect
        // FIXME: Rare java.lang.IndexOutOfBoundsException: Index: 37, Size: 35
        val firstItem = items[visibleItems.first().index] ?: return@LaunchedEffect
        val nextItems = visibleItems.takeLast(visibleItems.size - 1)
        onIntent(LogIntent.OnScroll(firstItem, nextItems, dayHeaderHeight))
    }

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            items.refresh()
        }
    }

    Box(modifier = modifier) {
        LogList(
            state = listState,
            items = items,
            onIntent = onIntent,
            modifier = Modifier.fillMaxSize(),
        )
        LogDay(
            state = state.dayStickyInfo.dayState,
            modifier = Modifier
                .onGloballyPositioned { dayHeaderHeight = it.size.height }
                .offset { state.dayStickyInfo.offset }
                .background(
                    color = AppTheme.colors.scheme.background,
                    shape = RoundedCornerShape(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp),
                        bottomEnd = AppTheme.shapes.large.bottomEnd,
                        bottomStart = CornerSize(0.dp),
                    ),
                )
                .padding(all = AppTheme.dimensions.padding.P_3),
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
            monthLocalized = "",
            pagingData = flowOf(PagingData.from(emptyList())),
            dayStickyInfo = LogDayStickyInfo(
                dayState = LogDayState(
                    date = today(),
                    dayOfMonthLocalized = "01",
                    dayOfWeekLocalized = "Mon",
                    style = LogDayStyle(
                        isVisible = true,
                        isHighlighted = true,
                    )
                ),
            ),
            datePickerDialog = null,
        ),
        onIntent = {},
    )
}