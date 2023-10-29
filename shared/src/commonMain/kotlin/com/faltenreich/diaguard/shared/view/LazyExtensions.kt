package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

inline fun <T> LazyListScope.itemsElse(
    items: List<T>?,
    sizeIfNull: Int = 10,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T?) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T?) -> Unit
) {
    when (items) {
        null -> items(
            count = sizeIfNull,
            key = { it },
            contentType = { contentType(null) },
            itemContent = { itemContent(null) },
        )
        // TODO: Display placeholder for empty items while avoiding IndexOutOfBoundsException
        else -> items(
            items = items,
            key = key,
            contentType = contentType,
            itemContent = itemContent,
        )
    }
}