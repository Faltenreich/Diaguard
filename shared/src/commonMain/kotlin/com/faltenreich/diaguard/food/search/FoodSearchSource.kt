package com.faltenreich.diaguard.food.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.paging.PagingConfig
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.logging.Logger

class FoodSearchSource(
    private val query: String,
    private val searchFood: SearchFoodUseCase,
) : PagingSource<PagingPage, Food>() {

    override fun getRefreshKey(state: PagingState<PagingPage, Food>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, Food> {
        val page = params.key ?: PagingPage(page = 0, pageSize = PAGE_SIZE)
        Logger.debug("Loading food for query \"$query\" at page $page")
        val food = searchFood(query, page)
        Logger.debug("Loaded ${food.size} food for query \"$query\" at page $page")
        return LoadResult.Page(
            data = food,
            prevKey = null,
            nextKey = if (food.isNotEmpty()) page + 1 else null,
        )
    }

    companion object {

        private const val PAGE_SIZE = 20

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE)
        }
    }
}