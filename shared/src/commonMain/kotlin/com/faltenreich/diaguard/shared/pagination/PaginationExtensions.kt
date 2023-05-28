package com.faltenreich.diaguard.shared.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun LazyListState.onPagination(
    buffer : Int = 0,
    condition: Boolean,
    onPagination : (direction: PaginationDirection) -> Unit,
): LazyListState {
    require(buffer >= 0) { "Buffer must not be negative but was $buffer" }
    if (!condition) {
        return this
    }

    val reachedEnd = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }
    LaunchedEffect(reachedEnd){
        snapshotFlow { reachedEnd.value }
            .distinctUntilChanged()
            .collect {
                onPagination(PaginationDirection.END)
            }
    }
    val reachedStart = remember {
        derivedStateOf {
            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()
                ?: return@derivedStateOf true
            firstVisibleItem.index <= buffer
        }
    }
    LaunchedEffect(reachedStart){
        snapshotFlow { reachedStart.value }
            .distinctUntilChanged()
            .collect {
                onPagination(PaginationDirection.START)
            }
    }
    return this
}