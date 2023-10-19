package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one medical property of the human body
 */
data class MeasurementProperty(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: DatabaseKey.MeasurementProperty?,
    val name: String,
    val icon: String?,
    val sortIndex: Long,
) : DatabaseEntity, Seedable {

    lateinit var types: List<MeasurementType>

    val isBloodSugar: Boolean
        get() = key == DatabaseKey.MeasurementProperty.BLOOD_SUGAR
}