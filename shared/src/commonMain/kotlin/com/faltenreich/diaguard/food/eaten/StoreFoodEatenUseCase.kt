package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.entry.Entry

class StoreFoodEatenUseCase(private val repository: FoodEatenRepository) {

    operator fun invoke(
        foodEaten: List<FoodEatenInputState>,
        entry: Entry.Local,
    ) {
        val foodEatenBefore = repository.getByEntryId(entry.id)
        createOrUpdateFoodEaten(foodEaten, foodEatenBefore, entry)
        deleteFoodEatenIfObsolete(foodEaten, foodEatenBefore)
    }

    private fun createOrUpdateFoodEaten(
        foodEaten: List<FoodEatenInputState>,
        foodEatenBefore: List<FoodEaten.Local>,
        entry: Entry.Local,
    ) {
        foodEaten.forEach { now ->
            val amountInGrams = now.amountInGrams ?: return@forEach
            val legacy = foodEatenBefore.firstOrNull { before -> before.food.id == now.food.id }
            when (legacy) {
                null -> repository.create(
                    FoodEaten.Intermediate(
                        amountInGrams = amountInGrams,
                        food = now.food,
                        entry = entry,
                    )
                )
                else -> repository.update(legacy.copy(amountInGrams = amountInGrams))
            }
        }
    }

    private fun deleteFoodEatenIfObsolete(
        foodEaten: List<FoodEatenInputState>,
        foodEatenBefore: List<FoodEaten.Local>,
    ) {
        foodEatenBefore
            .filterNot { before -> foodEaten.any { now -> now.food.id == before.food.id } }
            .forEach { notAnymore -> repository.delete(notAnymore) }
    }
}