package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

inline fun <T> LazyListScope.itemsOptional(
    items: List<T>?,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T?) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T?) -> Unit
) {
    when {
        items == null -> items(
            count = 10,
            key = { it },
            contentType = { contentType(null) },
        ) {
            itemContent(null)
        }
        // FIXME: throws IndexOutOfBoundsException
        items.isEmpty() -> item {
            Text("Nothing found")
        }
        else -> items(
            items = items,
            key = key,
            contentType = contentType,
            itemContent = itemContent,
        )
    }
}