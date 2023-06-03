package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

expect class PaginationItems<T : Any> {

    val itemCount: Int

    fun peek(index: Int): T?
}

@Composable
expect fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(
    context: CoroutineContext = EmptyCoroutineContext,
): PaginationItems<T>

expect fun <T : Any> LazyListScope.items(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
)

expect fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope,
): Flow<PagingData<T>>