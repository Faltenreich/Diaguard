package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.networking.RemoteEntity

/**
 * Entity representing something that can be eaten or drunk
 */
data class Food(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val uuid: String?,
    val name: String,
    val brand: String?,
    val ingredients: String?,
    val labels: String?,
    val carbohydrates: Double,
    val energy: Double?,
    val fat: Double?,
    val fatSaturated: Double?,
    val fiber: Double?,
    val proteins: Double?,
    val salt: Double?,
    val sodium: Double?,
    val sugar: Double?,
) : DatabaseEntity, RemoteEntity