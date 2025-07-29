package com.faltenreich.diaguard.food.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.paging.PagingConfig
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.first

class FoodSearchSource(
    private val searchParams: FoodSearchParams,
    private val searchFood: SearchFoodUseCase,
) : PagingSource<PagingPage, Food.Localized>() {

    override fun getRefreshKey(state: PagingState<PagingPage, Food.Localized>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, Food.Localized> {
        val page = params.key ?: PagingPage(page = 0, pageSize = params.loadSize)
        Logger.debug("Loading food for query \"${searchParams.query}\" at page $page")
        val food = searchFood(searchParams, page).first()
        Logger.debug("Loaded ${food.size} food for query \"${searchParams.query}\" at page $page")
        return LoadResult.Page(
            data = food,
            prevKey = if (page.page > 0) page - 1 else null,
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