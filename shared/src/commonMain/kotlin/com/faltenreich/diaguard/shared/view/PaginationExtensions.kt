package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

expect class PaginationItems<T : Any>

@Composable
expect fun <T : Any> Flow<PagingData<T>>.collectAsPaginationItems(): PaginationItems<T>

expect fun <T : Any> LazyListScope.paginationItems(
    items: PaginationItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
)