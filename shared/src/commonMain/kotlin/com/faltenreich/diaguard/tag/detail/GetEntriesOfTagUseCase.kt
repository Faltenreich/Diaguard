package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.tag.Tag

class GetEntriesOfTagUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val mapEntryListItemState: MapEntryListItemStateUseCase,
) {

    suspend operator fun invoke(tag: Tag.Local, page: PagingPage): List<EntryListItemState> {
        return entryRepository.getByTagId(tag.id, page).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(id)
                entryTags = entryTagRepository.getByEntryId(id)
                foodEaten = foodEatenRepository.getByEntryId(id)
            }
        }.map { entry -> mapEntryListItemState(entry) }
    }
}