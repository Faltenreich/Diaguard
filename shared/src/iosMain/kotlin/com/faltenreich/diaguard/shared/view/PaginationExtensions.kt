package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import app.cash.paging.PagingSourceLoadParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

typealias PaginationItems<T> = Error

@Composable
actual fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(
    context: CoroutineContext,
): PaginationItems<T> {
    TODO("Not yet implemented")
}

actual fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope,
): Flow<PagingData<T>> {
    TODO("Not yet implemented")
}

actual fun <T : Any> PagingSourceLoadParams<T>.isRefreshing(): Boolean {
    TODO("Not yet implemented")
}

actual fun <T : Any> PagingSourceLoadParams<T>.isAppending(): Boolean {
    TODO("Not yet implemented")
}

actual fun <T : Any> PagingSourceLoadParams<T>.isPrepending(): Boolean {
    TODO("Not yet implemented")
}

actual fun <T : Any> PagingData<T>.insertHeaderItem(
    terminalSeparatorType: TerminalSeparatorType,
    item: T,
): PagingData<T> {
    TODO("Not yet implemented")
}

actual fun  <T : R, R : Any> PagingData<T>.insertSeparators(
    terminalSeparatorType: TerminalSeparatorType,
    generator: suspend (T?, T?) -> R?,
): PagingData<R> {
    TODO("Not yet implemented")
}