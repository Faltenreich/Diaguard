package com.faltenreich.diaguard.entry.list

import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject

class EntryListPagingSource(
    private val getEntries: (page: Int) -> List<Entry.Local>,
    private val valueRepository: MeasurementValueRepository = inject(),
    private val entryTagRepository: EntryTagRepository = inject(),
    private val foodEatenRepository: FoodEatenRepository = inject(),
) : PagingSource<Int, Entry.Local>() {

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Entry.Local>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Entry.Local> {
        val page = params.key ?: 0

        val entries = getEntries(page).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(entry.id)
                entryTags = entryTagRepository.getByEntryId(entry.id)
                foodEaten = foodEatenRepository.getByEntryId(entry.id)
            }
        }

        return PagingSourceLoadResultPage(
            data = entries,
            prevKey = null,
            nextKey = page + 1,
        )
    }

    companion object {

        private const val PAGE_SIZE = 10

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE)
        }
    }
}