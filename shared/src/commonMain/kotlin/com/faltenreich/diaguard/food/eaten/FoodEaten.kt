package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.database.DatabaseEntity

/**
 * Entity representing food that has been eaten at one point in time
 */
sealed interface FoodEaten {

    // TODO: Change to Double
    val amountInGrams: Long
    val food: Food.Local
    val entry: Entry.Local

    data class Legacy(
        val createdAt: DateTime,
        val updatedAt: DateTime,
        override val amountInGrams: Long,
        val foodId: Long,
        val mealId: Long,
    ) : FoodEaten {

        override lateinit var food: Food.Local
        override lateinit var entry: Entry.Local
    }

    data class Intermediate(
        override val amountInGrams: Long,
        override val food: Food.Local,
        override val entry: Entry.Local,
    ) : FoodEaten

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val amountInGrams: Long,
        override val food: Food.Local,
        override val entry: Entry.Local,
    ) : FoodEaten, DatabaseEntity
}