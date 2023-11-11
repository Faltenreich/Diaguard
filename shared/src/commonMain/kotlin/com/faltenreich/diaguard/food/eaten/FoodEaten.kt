package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing food that has been eaten at one point in time
 */
data class FoodEaten(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val amountInGrams: Double,
    val foodId: Long,
    val entryId: Long,
) : DatabaseEntity {

    lateinit var food: Food
    lateinit var entry: Entry
}