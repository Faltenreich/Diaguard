package com.faltenreich.diaguard.backup.legacy.dao

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong

class LegacyFoodEatenSqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getFoodEaten(): List<FoodEaten.Legacy> {
        val foodEaten = mutableListOf<FoodEaten.Legacy>()
        database.query("foodeaten") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val foodId = getLong("food") ?: return@query
            val mealId = getLong("meal") ?: return@query
            val amountInGrams = getDouble("amountInGrams") ?: return@query
            foodEaten.add(
                FoodEaten.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    amountInGrams = amountInGrams,
                    foodId = foodId,
                    mealId = mealId,
                )
            )
        }
        return foodEaten
    }
}