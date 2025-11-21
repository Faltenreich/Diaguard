package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.data.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.view.paging.PagingPage

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val mapEntryListItemState: MapEntryListItemStateUseCase,
) {

    suspend operator fun invoke(query: String, page: PagingPage): List<EntryListItemState> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            entryRepository.getByQuery(query, page).map { entry ->
                entry.apply {
                    values = valueRepository.getByEntryId(entry.id)
                    entryTags = entryTagRepository.getByEntryId(entry.id)
                    foodEaten = foodEatenRepository.getByEntryId(entry.id)
                }
            }.map { entry -> mapEntryListItemState(entry, includeDate = true) }
        }
    }
}