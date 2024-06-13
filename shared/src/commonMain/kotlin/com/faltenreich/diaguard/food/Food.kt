package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.networking.RemoteEntity

/**
 * Entity representing something that can be eaten or drunk
 */
sealed interface Food {

    val name: String
    val brand: String?
    val ingredients: String?
    val labels: String?
    val carbohydrates: Double
    val energy: Double?
    val fat: Double?
    val fatSaturated: Double?
    val fiber: Double?
    val proteins: Double?
    val salt: Double?
    val sodium: Double?
    val sugar: Double?

    data class Legacy(
        val id: Long,
        val createdAt: DateTime,
        val updatedAt: DateTime,
        override val uuid: String?,
        override val name: String,
        override val brand: String?,
        override val ingredients: String?,
        override val labels: String?,
        override val carbohydrates: Double,
        override val energy: Double?,
        override val fat: Double?,
        override val fatSaturated: Double?,
        override val fiber: Double?,
        override val proteins: Double?,
        override val salt: Double?,
        override val sodium: Double?,
        override val sugar: Double?,
    ) : Food, RemoteEntity

    data class User(
        override val name: String,
        override val brand: String?,
        override val ingredients: String?,
        override val labels: String?,
        override val carbohydrates: Double,
        override val energy: Double?,
        override val fat: Double?,
        override val fatSaturated: Double?,
        override val fiber: Double?,
        override val proteins: Double?,
        override val salt: Double?,
        override val sodium: Double?,
        override val sugar: Double?,
    ) : Food

    data class Remote(
        override val uuid: String?,
        override val name: String,
        override val brand: String?,
        override val ingredients: String?,
        override val labels: String?,
        override val carbohydrates: Double,
        override val energy: Double?,
        override val fat: Double?,
        override val fatSaturated: Double?,
        override val fiber: Double?,
        override val proteins: Double?,
        override val salt: Double?,
        override val sodium: Double?,
        override val sugar: Double?,
    ) : Food, RemoteEntity

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val uuid: String?,
        override val name: String,
        override val brand: String?,
        override val ingredients: String?,
        override val labels: String?,
        override val carbohydrates: Double,
        override val energy: Double?,
        override val fat: Double?,
        override val fatSaturated: Double?,
        override val fiber: Double?,
        override val proteins: Double?,
        override val salt: Double?,
        override val sodium: Double?,
        override val sugar: Double?,
    ) : Food, DatabaseEntity, RemoteEntity
}