package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class FoodEatenSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val foodMapper: FoodSqlDelightMapper,
    private val entryMapper: EntrySqlDelightMapper,
) {

    fun map(
        foodEatenId: Long,
        foodEatenCreatedAt: String,
        foodEatenUpdatedAt: String,
        foodEatenAmountInGrams: Long,
        foodEatenFoodId: Long,
        foodEatenEntryId: Long,
        foodId: Long,
        foodCreatedAt: String,
        foodUpdatedAt: String,
        foodName: String,
        foodBrand: String?,
        foodIngredients: String?,
        foodLabels: String?,
        foodCarbohydrates: Double,
        foodEnergy: Double?,
        foodFat: Double?,
        foodFatSaturated: Double?,
        foodFiber: Double?,
        foodProteins: Double?,
        foodSalt: Double?,
        foodSodium: Double?,
        foodSugar: Double?,
        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,
    ): FoodEaten {
        return FoodEaten(
            id = foodEatenId,
            createdAt = dateTimeFactory.dateTime(isoString = foodEatenCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = foodEatenUpdatedAt),
            amountInGrams = foodEatenAmountInGrams,
            foodId = foodEatenFoodId,
            entryId = foodEatenEntryId,
        ).apply {
            food = foodMapper.map(
                id = foodId,
                createdAt = foodCreatedAt,
                updatedAt = foodUpdatedAt,
                name = foodName,
                brand = foodBrand,
                ingredients = foodIngredients,
                labels = foodLabels,
                carbohydrates = foodCarbohydrates,
                energy = foodEnergy,
                fat = foodFat,
                fatSaturated = foodFatSaturated,
                fiber = foodFiber,
                proteins = foodProteins,
                salt = foodSalt,
                sodium = foodSodium,
                sugar = foodSugar,
            )
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            )
        }
    }
}