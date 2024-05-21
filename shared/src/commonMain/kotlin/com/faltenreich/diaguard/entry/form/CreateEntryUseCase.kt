package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.tag.CreateEntryTagsUseCase

class CreateEntryUseCase(
    private val entryRepository: EntryRepository,
    private val createMeasurementValues: CreateMeasurementValuesUseCase,
    private val storeFoodEaten: StoreFoodEatenUseCase,
    private val createEntryTags: CreateEntryTagsUseCase,
) {

    operator fun invoke(input: EntryFormInput) = with(input) {
        val entryId = id?.also {
            entryRepository.update(
                id = id,
                dateTime = dateTime,
                note = note,
            )
        } ?: entryRepository.create(Entry.User(dateTime = dateTime, note = note))
        val entry = checkNotNull(entryRepository.getById(entryId))
        createMeasurementValues(measurements, entry)
        storeFoodEaten(foodEaten, entry)
        createEntryTags(tags, entry)
    }
}