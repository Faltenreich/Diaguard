package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.food.eaten.CreateFoodEatenUseCase
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.tag.CreateEntryTagsUseCase
import com.faltenreich.diaguard.tag.Tag

class CreateEntryUseCase(
    private val entryRepository: EntryRepository,
    private val createMeasurementValues: CreateMeasurementValuesUseCase,
    private val createFoodEaten: CreateFoodEatenUseCase,
    private val createEntryTags: CreateEntryTagsUseCase,
) {

    operator fun invoke(
        id: Long?,
        dateTime: DateTime,
        note: String?,
        measurements: List<MeasurementPropertyInputData>,
        foodEaten: List<FoodEatenInputData>,
        tags: List<Tag>,
    ) {
        val entryId = id?.also {
            entryRepository.update(
                id = id,
                dateTime = dateTime,
                note = note,
            )
        } ?: entryRepository.create(dateTime)
        createMeasurementValues(measurements, entryId)
        createFoodEaten(foodEaten, entryId)
        createEntryTags(tags, entryId)
    }
}