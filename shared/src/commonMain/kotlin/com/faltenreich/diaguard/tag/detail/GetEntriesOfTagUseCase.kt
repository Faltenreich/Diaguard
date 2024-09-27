package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEntriesOfTagUseCase(
    private val entryTagRepository: EntryTagRepository,
    private val valueRepository: MeasurementValueRepository,
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(tag: Tag.Local): Flow<List<EntryTag.Local>> {
        return entryTagRepository.observeByTagId(tag.id).map { entryTags ->
            entryTags.onEach { entryTag ->
                entryTag.entry.apply {
                    values = valueRepository.getByEntryId(id)
                    this.entryTags = entryTagRepository.getByEntryId(id)
                    foodEaten = foodEatenRepository.getByEntryId(id)
                }
            }
        }
    }
}