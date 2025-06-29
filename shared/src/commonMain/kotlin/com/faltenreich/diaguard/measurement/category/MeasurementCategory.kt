package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

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

        val isBloodSugar: Boolean
            get() = key == DatabaseKey.MeasurementCategory.BLOOD_SUGAR

        val isMeal: Boolean
            get() = key == DatabaseKey.MeasurementCategory.MEAL

        val isUserGenerated: Boolean
            get() = key == null
    }
}