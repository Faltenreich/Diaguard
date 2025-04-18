package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseEntity

/**
 * Entity representing one entry at a given point in time
 */
sealed interface Entry {

    val dateTime: DateTime
    val note: String?

    data class Legacy(
        val id: Long,
        val createdAt: DateTime,
        val updatedAt: DateTime,
        override val dateTime: DateTime,
        override val note: String?,
    ) : Entry

    data class User(
        override val dateTime: DateTime,
        override val note: String?,
    ) : Entry

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val dateTime: DateTime,
        override val note: String?,
    ) : Entry, DatabaseEntity {

        lateinit var values: List<MeasurementValue>
        lateinit var entryTags: List<EntryTag>
        lateinit var foodEaten: List<FoodEaten>
    }
}