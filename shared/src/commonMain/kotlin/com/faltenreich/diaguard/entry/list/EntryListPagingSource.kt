package com.faltenreich.diaguard.entry.list

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.grams_abbreviation
import kotlinx.coroutines.flow.first

class EntryListPagingSource(
    private val getEntries: (PagingPage) -> List<Entry.Local>,
    private val valueRepository: MeasurementValueRepository = inject(),
    private val entryTagRepository: EntryTagRepository = inject(),
    private val foodEatenRepository: FoodEatenRepository = inject(),
    private val getPreference: GetPreferenceUseCase = inject(),
    private val dateTimeFormatter: DateTimeFormatter = inject(),
    private val numberFormatter: NumberFormatter = inject(),
    private val localization: Localization = inject(),
) : PagingSource<PagingPage, EntryListItemState>() {

    override fun getRefreshKey(state: PagingState<PagingPage, EntryListItemState>): PagingPage? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<PagingPage>): LoadResult<PagingPage, EntryListItemState> {
        val page = params.key ?: PagingPage(page = 0, pageSize = params.loadSize)

        val entries = getEntries(page).map { entry ->
            EntryListItemState(
                entry = entry.apply {
                    values = valueRepository.getByEntryId(entry.id)
                    entryTags = entryTagRepository.getByEntryId(entry.id)
                    foodEaten = foodEatenRepository.getByEntryId(entry.id)
                },
                dateTimeLocalized = "%s, %s".format(
                    dateTimeFormatter.formatMonth(entry.dateTime.date.month, abbreviated = true),
                    dateTimeFormatter.formatDateTime(entry.dateTime),
                ),
                foodEatenLocalized = entry.foodEaten.map { foodEaten ->
                    "%s %s %s".format(
                        numberFormatter(
                            number = foodEaten.amountInGrams,
                            // TODO: Observe Flow safely
                            scale = getPreference(DecimalPlaces).first(),
                            locale = localization.getLocale(),
                        ),
                        localization.getString(Res.string.grams_abbreviation),
                        foodEaten.food.name,
                    )
                },
            )
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