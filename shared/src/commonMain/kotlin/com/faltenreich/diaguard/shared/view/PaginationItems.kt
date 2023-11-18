package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import app.cash.paging.PagingSourceLoadParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

expect class PaginationItems<T : Any> {

    val itemCount: Int

    fun peek(index: Int): T?

    fun get(index: Int): T?
}

@Composable
expect fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(
    context: CoroutineContext = EmptyCoroutineContext,
): PaginationItems<T>

expect fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope,
): Flow<PagingData<T>>

expect fun <T : Any> PagingSourceLoadParams<T>.isRefreshing(): Boolean

expect fun <T : Any> PagingSourceLoadParams<T>.isAppending(): Boolean

expect fun <T : Any> PagingSourceLoadParams<T>.isPrepending(): Boolean