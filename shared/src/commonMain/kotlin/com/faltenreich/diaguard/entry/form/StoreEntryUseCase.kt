package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.entry.tag.StoreEntryTagsUseCase
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.usecase.StoreMeasurementValuesUseCase

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

    operator fun invoke(input: EntryListItemState) {
        val entryId = repository.create(
            Entry.User(
                dateTime = input.entry.dateTime,
                note = input.entry.note,
            )
        )
        val entry = checkNotNull(repository.getById(entryId))

        val values = input.entry.values.map { value ->
            MeasurementValue.User(
                value = value.value,
                property = value.property,
                entry = entry,
            )
        }
        storeMeasurementValues(values)

        val foodEaten = input.entry.foodEaten.map { foodEaten ->
            FoodEaten.Intermediate(
                amountInGrams = foodEaten.amountInGrams,
                food = foodEaten.food,
                entry = entry,
            )
        }
        storeFoodEaten(foodEaten)

        val tags = input.entry.entryTags.map(EntryTag::tag)
        storeEntryTags(tags, entry)
    }
}