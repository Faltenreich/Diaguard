package com.faltenreich.diaguard.shared.view

import androidx.paging.PagingSource.LoadParams

fun <T : Any> LoadParams<T>.isRefreshing(): Boolean {
    return this is LoadParams.Refresh<T>
}

fun <T : Any> LoadParams<T>.isAppending(): Boolean {
    return this is LoadParams.Append<T>
}

fun <T : Any> LoadParams<T>.isPrepending(): Boolean {
    return this is LoadParams.Prepend<T>
}