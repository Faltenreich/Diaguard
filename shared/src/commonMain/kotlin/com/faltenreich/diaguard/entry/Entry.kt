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
    ) : Entry, DatabaseEntity

    data class Localized(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val dateTime: DateTime,
        override val note: String?,
        val dateTimeFormatted: String,
        val timeFormatted: String,
        val dateFormatted: String,
        val values: List<MeasurementValue>,
        val entryTags: List<EntryTag>,
        val foodEaten: List<FoodEaten>,
    ) : Entry, DatabaseEntity
}