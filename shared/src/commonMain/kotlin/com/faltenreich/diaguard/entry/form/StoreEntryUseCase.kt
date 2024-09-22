package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.StoreEntryTagsUseCase
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.measurement.value.StoreMeasurementValuesUseCase

class StoreEntryUseCase(
    private val repository: EntryRepository,
    private val storeMeasurementValues: StoreMeasurementValuesUseCase,
    private val storeFoodEaten: StoreFoodEatenUseCase,
    private val storeEntryTags: StoreEntryTagsUseCase,
) {

    operator fun invoke(input: EntryFormInput) = with(input) {
        val entry = if (entry != null) {
            val entry = entry.copy(
                dateTime = dateTime,
                note = note,
            )
            repository.update(entry)
            entry
        } else {
            val entry = Entry.User(
                dateTime = dateTime,
                note = note,
            )
            val entryId = repository.create(entry)
            checkNotNull(repository.getById(entryId))
        }
        storeMeasurementValues(measurements, entry)
        storeFoodEaten(foodEaten, entry)
        storeEntryTags(tags, entry)
    }
}