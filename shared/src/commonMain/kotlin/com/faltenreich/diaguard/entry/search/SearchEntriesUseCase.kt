package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(query: String, page: Int): List<Entry.Local> {
        // TODO: Support page
        if (page > 0) return emptyList()

        return if (query.isBlank()) {
            emptyList()
        } else {
            entryRepository.getByQuery(query).map { entry ->
                entry.apply {
                    values = valueRepository.getByEntryId(entry.id)
                    entryTags = entryTagRepository.getByEntryId(entry.id)
                    foodEaten = foodEatenRepository.getByEntryId(entry.id)
                }
            }
        }

    }
}