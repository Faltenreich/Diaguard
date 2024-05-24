package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.eaten.FoodEaten

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
        @Suppress("UNUSED_PARAMETER") foodEatenFoodId: Long,
        @Suppress("UNUSED_PARAMETER") foodEatenEntryId: Long,

        foodId: Long,
        foodCreatedAt: String,
        foodUpdatedAt: String,
        foodUuid: String?,
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
    ): FoodEaten.Local {
        return FoodEaten.Local(
            id = foodEatenId,
            createdAt = dateTimeFactory.dateTime(isoString = foodEatenCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = foodEatenUpdatedAt),
            amountInGrams = foodEatenAmountInGrams,
            food = foodMapper.map(
                id = foodId,
                createdAt = foodCreatedAt,
                updatedAt = foodUpdatedAt,
                uuid = foodUuid,
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
            ),
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            ),
        )
    }
}