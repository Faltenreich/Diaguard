package com.faltenreich.diaguard.data.food.eaten

import com.faltenreich.diaguard.data.DatabaseEntity
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.datetime.DateTime

/**
 * Entity representing food that has been eaten at one point in time
 */
sealed interface FoodEaten {

    val amountInGrams: Double
    val food: Food.Local
    val entry: Entry.Local

    data class Legacy(
        val createdAt: DateTime,
        val updatedAt: DateTime,
        override val amountInGrams: Double,
        val foodId: Long,
        val mealId: Long,
    ) : FoodEaten {

        override lateinit var food: Food.Local
        override lateinit var entry: Entry.Local
    }

    data class Intermediate(
        override val amountInGrams: Double,
        override val food: Food.Local,
        override val entry: Entry.Local,
    ) : FoodEaten

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val amountInGrams: Double,
        override val food: Food.Local,
        override val entry: Entry.Local,
    ) : FoodEaten, DatabaseEntity

    data class Localized(
        val local: Local,
        val dateTime: String,
        val amountInGrams: String,
    )
}