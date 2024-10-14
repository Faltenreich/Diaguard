package com.faltenreich.diaguard.shared.view

import androidx.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams

fun <T : Any> PagingSourceLoadParams<T>.isRefreshing(): Boolean {
    return this is PagingSource.LoadParams.Refresh<T>
}

fun <T : Any> PagingSourceLoadParams<T>.isAppending(): Boolean {
    return this is PagingSource.LoadParams.Append<T>
}

fun <T : Any> PagingSourceLoadParams<T>.isPrepending(): Boolean {
    return this is PagingSource.LoadParams.Prepend<T>
}