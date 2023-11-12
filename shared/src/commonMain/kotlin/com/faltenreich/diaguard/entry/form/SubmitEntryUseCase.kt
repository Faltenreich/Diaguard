package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateTime

class SubmitEntryUseCase(
    private val entryRepository: EntryRepository,
    private val measurementValueRepository: MeasurementValueRepository,
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(
        id: Long?,
        dateTime: DateTime,
        note: String?,
        measurements: List<MeasurementTypeInputData>,
        foodEaten: List<FoodEatenInputData>,
    ) {
        val entryId = id ?: entryRepository.create(dateTime)
        entryRepository.update(
            id = entryId,
            dateTime = dateTime,
            note = note,
        )
        val values = measurementValueRepository.getByEntryId(entryId)
        measurements.forEach { (type, input) ->
            // TODO: Validate and normalize by unit
            val value = values.firstOrNull { it.typeId == type.id }
            val normalized = input.toDoubleOrNull() ?: return@forEach
            measurementValueRepository.update(
                id = value?.id ?: measurementValueRepository.create(
                    value = normalized,
                    typeId = type.id,
                    entryId = entryId,
                ),
                value = normalized,
            )
        }
        // TODO: Override legacy to prevent duplicates
        foodEaten.forEach { data ->
            val amountInGrams = data.amountInGrams ?: return@forEach
            foodEatenRepository.create(
                amountInGrams = amountInGrams,
                foodId = data.food.id,
                entryId = entryId,
            )
        }
    }
}