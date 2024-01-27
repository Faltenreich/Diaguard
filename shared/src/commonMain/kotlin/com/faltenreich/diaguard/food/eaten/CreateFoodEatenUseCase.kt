package com.faltenreich.diaguard.food.eaten

class CreateFoodEatenUseCase(
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(
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