package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import app.cash.paging.PagingSourceLoadParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

actual class PaginationItems<T : Any> {

    actual val itemCount: Int
        get() = TODO("Not yet implemented")

    actual fun peek(index: Int): T? {
        TODO("Not yet implemented")
    }

    actual fun get(index: Int): T? {
        TODO("Not yet implemented")
    }
}

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