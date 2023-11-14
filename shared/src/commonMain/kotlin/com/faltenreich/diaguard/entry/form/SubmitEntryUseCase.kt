package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.food.eaten.FoodEaten
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
        measurements: List<MeasurementPropertyInputData>,
        foodEaten: List<FoodEatenInputData>,
    ) {
        val entryId = submitEntry(id, dateTime, note)
        submitMeasurements(measurements, entryId)
        submitFoodEaten(foodEaten, entryId)
    }

    private fun submitEntry(
        id: Long?,
        dateTime: DateTime,
        note: String?,
    ): Long {
        val entryId = id ?: entryRepository.create(dateTime)
        entryRepository.update(
            id = entryId,
            dateTime = dateTime,
            note = note,
        )
        return entryId
    }

    private fun submitMeasurements(
        measurements: List<MeasurementPropertyInputData>,
        entryId: Long,
    ) {
        // TODO: Delete newly removed values
        val values = measurements.flatMap(MeasurementPropertyInputData::typeInputDataList)
        val valuesFromBefore = measurementValueRepository.getByEntryId(entryId)
        values.forEach { (type, input) ->
            // TODO: Validate and normalize by unit
            val legacyId = valuesFromBefore.firstOrNull { it.typeId == type.id }?.id
            val normalized = input.toDoubleOrNull()
            if (normalized != null) {
                measurementValueRepository.update(
                    id = legacyId ?: measurementValueRepository.create(
                        value = normalized,
                        typeId = type.id,
                        entryId = entryId,
                    ),
                    value = normalized,
                )
            } else {
                val obsolete = valuesFromBefore.firstOrNull { it.typeId == type.id }
                if (obsolete != null) {
                    measurementValueRepository.deleteById(obsolete.id)
                }
            }
        }
    }

    private fun submitFoodEaten(
        foodEaten: List<FoodEatenInputData>,
        entryId: Long,
    ) {
        val foodEatenBefore = foodEatenRepository.getByEntryId(entryId)
        createOrUpdateFoodEaten(foodEaten, foodEatenBefore, entryId)
        deleteFoodEatenIfObsolete(foodEaten, foodEatenBefore)
    }

    private fun createOrUpdateFoodEaten(
        foodEaten: List<FoodEatenInputData>,
        foodEatenBefore: List<FoodEaten>,
        entryId: Long,
    ) {
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
    }

    private fun deleteFoodEatenIfObsolete(
        foodEaten: List<FoodEatenInputData>,
        foodEatenBefore: List<FoodEaten>,
    ) {
        foodEatenBefore
            .filterNot { before -> foodEaten.any { now -> now.food.id == before.food.id } }
            .forEach { notAnymore -> foodEatenRepository.deleteById(notAnymore.id) }
    }
}