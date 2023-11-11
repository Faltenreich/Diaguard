package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class FoodEatenSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        amountInGrams: Long,
        foodId: Long,
        entryId: Long,
    ): FoodEaten {
        return FoodEaten(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            amountInGrams = amountInGrams,
            foodId = foodId,
            entryId = entryId,
        )
    }
}