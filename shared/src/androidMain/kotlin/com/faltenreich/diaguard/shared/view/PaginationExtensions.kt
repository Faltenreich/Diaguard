package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

actual typealias PaginationItems<T> = LazyPagingItems<T>

@Composable
actual fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(
    context: CoroutineContext,
): PaginationItems<T> {
    return collectAsLazyPagingItems()
}

actual fun <T : Any> LazyListScope.paginationItems(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)?,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(
        items = items,
        key = key,
        itemContent = itemContent,
    )
}

actual fun <T : Any> Flow<PagingData<T>>.cachedIn(scope: CoroutineScope): Flow<PagingData<T>> {
    return cachedIn(scope = scope)
}