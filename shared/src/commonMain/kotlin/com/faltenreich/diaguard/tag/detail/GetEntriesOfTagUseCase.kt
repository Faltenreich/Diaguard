package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.tag.Tag

class GetEntriesOfTagUseCase(
    private val entryTagRepository: EntryTagRepository,
    private val valueRepository: MeasurementValueRepository,
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(tag: Tag.Local): List<Entry.Local> {
        // FIXME: Avoid duplicates
        return entryTagRepository.getByTagId(tag.id).map { entryTag ->
            entryTag.entry.apply {
                values = valueRepository.getByEntryId(id)
                entryTags = entryTagRepository.getByEntryId(id)
                foodEaten = foodEatenRepository.getByEntryId(id)
            }
        }
    }
}