package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

typealias PaginationItems<T> = Error

@Composable
actual fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(): PaginationItems<T> {
    TODO("Not yet implemented")
}

actual fun <T : Any> LazyListScope.paginationItems(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)?,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    TODO("Not yet implemented")
}