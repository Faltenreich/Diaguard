package com.faltenreich.diaguard.food.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.paging.PagingConfig
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject

class FoodSearchSource(
    private val query: String,
    private val repository: FoodRepository = inject(),
) : PagingSource<PagingPage, Food>() {

    override fun getRefreshKey(state: PagingState<PagingPage, Food>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, Food> {
        val page = params.key ?: PagingPage(page = 0, pageSize = PAGE_SIZE)
        val food = repository.getByQuery(query, page)
        return LoadResult.Page(
            data = food,
            prevKey = null,
            nextKey = page + 1,
        )
    }

    companion object {

        private const val PAGE_SIZE = 20

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE)
        }
    }
}