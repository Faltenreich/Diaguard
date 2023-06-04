package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import app.cash.paging.PagingSourceLoadParams
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

actual fun <T : Any> LazyListScope.items(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)?,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
    items(
        items = items,
        key = key,
        itemContent = itemContent,
    )
}

actual fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope,
): Flow<PagingData<T>> {
    return cachedIn(scope = scope)
}

actual fun <T : Any> PagingSourceLoadParams<T>.isRefreshing(): Boolean {
    return this is PagingSource.LoadParams.Refresh<T>
}

actual fun <T : Any> PagingSourceLoadParams<T>.isAppending(): Boolean {
    return this is PagingSource.LoadParams.Append<T>
}

actual fun <T : Any> PagingSourceLoadParams<T>.isPrepending(): Boolean {
    return this is PagingSource.LoadParams.Prepend<T>
}