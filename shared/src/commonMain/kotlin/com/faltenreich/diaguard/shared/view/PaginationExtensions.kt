package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.TerminalSeparatorType
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

expect fun <T : Any> LazyListScope.items(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
)

expect fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope,
): Flow<PagingData<T>>

expect fun <T : Any> PagingSourceLoadParams<T>.isRefreshing(): Boolean

expect fun <T : Any> PagingSourceLoadParams<T>.isAppending(): Boolean

expect fun <T : Any> PagingSourceLoadParams<T>.isPrepending(): Boolean

expect fun <T : Any> PagingData<T>.insertHeaderItem(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T
): PagingData<T>

expect fun  <T : R, R : Any> PagingData<T>.insertSeparators(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    generator: suspend (T?, T?) -> R?,
): PagingData<R>

fun <T : Any> PaginationItems<T>.firstIndexOrNull(condition: (T) -> Boolean): T? {
    TODO()
}