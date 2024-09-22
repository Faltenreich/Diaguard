package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.CreateEntryTagsUseCase
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase

class StoreEntryUseCase(
    private val entryRepository: EntryRepository,
    private val createMeasurementValues: CreateMeasurementValuesUseCase,
    private val storeFoodEaten: StoreFoodEatenUseCase,
    private val createEntryTags: CreateEntryTagsUseCase,
) {

    operator fun invoke(input: EntryFormInput) = with(input) {
        val entryId = entry?.let { editing ->
            val entry = editing.copy(
                dateTime = dateTime,
                note = note,
            )
            entryRepository.update(entry)
            entry.id
        } ?: entryRepository.create(
            Entry.User(
                dateTime = dateTime,
                note = note,
            )
        )
        val entry = checkNotNull(entryRepository.getById(entryId))
        createMeasurementValues(measurements, entry)
        storeFoodEaten(foodEaten, entry)
        createEntryTags(tags, entry)
    }
}