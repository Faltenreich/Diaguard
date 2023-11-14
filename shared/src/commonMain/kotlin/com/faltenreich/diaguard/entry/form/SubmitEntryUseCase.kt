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

        // TODO: Delete newly removed values and food eaten

        val valuesFromBefore = measurementValueRepository.getByEntryId(entryId)
        measurements.forEach { (type, input) ->
            // TODO: Validate and normalize by unit
            val legacyId = valuesFromBefore.firstOrNull { it.typeId == type.id }?.id
            val normalized = input.toDoubleOrNull() ?: return@forEach
            measurementValueRepository.update(
                id = legacyId ?: measurementValueRepository.create(
                    value = normalized,
                    typeId = type.id,
                    entryId = entryId,
                ),
                value = normalized,
            )
        }
        val foodEatenBefore = foodEatenRepository.getByEntryId(entryId)
        foodEaten.forEach { now ->
            val amountInGrams = now.amountInGrams ?: return@forEach
            val legacyId = foodEatenBefore.firstOrNull { before -> before.food.id == now.food.id }?.id
            foodEatenRepository.update(
                id = legacyId ?: foodEatenRepository.create(
                    amountInGrams = amountInGrams,
                    foodId = now.food.id,
                    entryId = entryId,
                ),
                amountInGrams = amountInGrams,
            )
        }
        foodEatenBefore
            .filterNot { before -> foodEaten.any { now -> now.food.id == before.food.id } }
            .forEach { notAnymore -> foodEatenRepository.deleteById(notAnymore.id) }
    }
}