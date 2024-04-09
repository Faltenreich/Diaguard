package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing one measurable category
 */
data class MeasurementCategory(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: DatabaseKey.MeasurementCategory?,
    val name: String,
    val icon: String?,
    val sortIndex: Long,
) : DatabaseEntity, Seedable {

    lateinit var properties: List<MeasurementProperty>

    val isBloodSugar: Boolean
        get() = key == DatabaseKey.MeasurementCategory.BLOOD_SUGAR

    val isMeal: Boolean
        get() = key == DatabaseKey.MeasurementCategory.MEAL
}