package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing the unit of a [MeasurementProperty]
 */
data class MeasurementUnit(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: DatabaseKey.MeasurementUnit?,
    val name: String,
    val abbreviation: String,
    val factor: Double,
    val isSelected: Boolean,
    val propertyId: Long,
) : DatabaseEntity, Seedable {

    lateinit var property: MeasurementProperty

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }
}