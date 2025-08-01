package com.faltenreich.diaguard.food.search

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.first

class FoodSearchSource(
    private val searchParams: FoodSearchParams,
    private val searchFood: SearchFoodUseCase,
) : PagingSource<Int, Food.Localized>() {

    override fun getRefreshKey(state: PagingState<Int, Food.Localized>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Food.Localized> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        val food = searchFood(searchParams, PagingPage(page, pageSize)).first()
        Logger.debug("Loaded food at page $page with page size $pageSize")

        return LoadResult.Page(
            data = food,
            prevKey = if (page > 0) page - 1 else null,
            nextKey = if (food.isNotEmpty()) page + 1 else null,
        )
    }

    companion object {

        private const val PAGE_SIZE = 20

        fun newConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
            )
        }
    }
}