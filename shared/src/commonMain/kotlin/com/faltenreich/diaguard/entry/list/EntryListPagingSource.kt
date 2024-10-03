package com.faltenreich.diaguard.entry.list

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject

class EntryListPagingSource(
    private val getEntries: (PagingPage) -> List<Entry.Local>,
    private val valueRepository: MeasurementValueRepository = inject(),
    private val entryTagRepository: EntryTagRepository = inject(),
    private val foodEatenRepository: FoodEatenRepository = inject(),
) : PagingSource<PagingPage, Entry.Local>() {

    override fun getRefreshKey(state: PagingState<PagingPage, Entry.Local>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, Entry.Local> {
        val page = params.key ?: PagingPage(page = 0, pageSize = params.loadSize)

        val entries = getEntries(page).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(entry.id)
                entryTags = entryTagRepository.getByEntryId(entry.id)
                foodEaten = foodEatenRepository.getByEntryId(entry.id)
            }
        }

        return LoadResult.Page(
            data = entries,
            prevKey = null,
            nextKey = if (entries.isNotEmpty()) page + 1 else null,
        )
    }

    companion object {

        private const val PAGE_SIZE = 10

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE)
        }
    }
}