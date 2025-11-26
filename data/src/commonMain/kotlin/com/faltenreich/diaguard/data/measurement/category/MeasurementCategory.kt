package com.faltenreich.diaguard.data.measurement.category

import com.faltenreich.diaguard.data.DatabaseEntity
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.datetime.DateTime

/**
 * Entity representing one measurable category
 */
sealed interface MeasurementCategory {

    val name: String
    val icon: String?
    val sortIndex: Long
    val isActive: Boolean

    data class Seed(
        override val name: String,
        override val icon: String?,
        override val sortIndex: Long,
        override val isActive: Boolean,
        val key: DatabaseKey.MeasurementCategory?,
        val properties: List<MeasurementProperty.Seed>,
    ) : MeasurementCategory
    
    data class User(
        override val name: String,
        override val icon: String?,
        override val sortIndex: Long,
        override val isActive: Boolean,
    ) : MeasurementCategory

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
        override val icon: String?,
        override val sortIndex: Long,
        override val isActive: Boolean,
        val key: DatabaseKey.MeasurementCategory?,
    ) : MeasurementCategory, DatabaseEntity {

        val isUserGenerated: Boolean = key == null

        val isMeal: Boolean = key == DatabaseKey.MeasurementCategory.MEAL
    }
}