package com.faltenreich.diaguard.entry.list

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.shared.data.PagingPage

class EntryListPagingSource(
    private val getData: suspend (PagingPage) -> List<EntryListItemState>,
) : PagingSource<PagingPage, EntryListItemState>() {

    override fun getRefreshKey(state: PagingState<PagingPage, EntryListItemState>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, EntryListItemState> {
        val page = params.key ?: PagingPage(page = 0, pageSize = params.loadSize)
        val data = getData(page)
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = if (data.isNotEmpty()) page + 1 else null,
        )
    }

    companion object {

        private const val PAGE_SIZE = 10

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE)
        }
    }
}