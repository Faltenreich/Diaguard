package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.food.eaten.CreateFoodEatenUseCase
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.tag.CreateEntryTagsUseCase

class CreateEntryUseCase(
    private val entryRepository: EntryRepository,
    private val createMeasurementValues: CreateMeasurementValuesUseCase,
    private val createFoodEaten: CreateFoodEatenUseCase,
    private val createEntryTags: CreateEntryTagsUseCase,
) {

    operator fun invoke(input: EntryFormInput) = with(input) {
        val entryId = id?.also {
            entryRepository.update(
                id = id,
                dateTime = dateTime,
                note = note,
            )
        } ?: entryRepository.create(dateTime)
        val entry = checkNotNull(entryRepository.getById(entryId))
        createMeasurementValues(measurements, entryId)
        createFoodEaten(foodEaten, entryId)
        createEntryTags(tags, entry)
    }
}