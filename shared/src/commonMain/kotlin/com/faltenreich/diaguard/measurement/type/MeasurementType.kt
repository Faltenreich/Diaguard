package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one type of [MeasurementProperty]
 */
data class MeasurementType(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: DatabaseKey.MeasurementType?,
    val name: String,
    val minimumValue: Double,
    val maximumValue: Double,
    val sortIndex: Long,
    val selectedUnitId: Long,
    val propertyId: Long,
) : DatabaseEntity, Seedable {

    lateinit var property: MeasurementProperty
    lateinit var units: List<MeasurementUnit>

    lateinit var selectedUnit: MeasurementUnit

    companion object {

        /**
         * Invalid id that acts as a temporary placeholder,
         * e.g. until the corresponding [MeasurementUnit] has been created
         */
        const val SELECTED_UNIT_ID_INVALID: Long = -1
    }
}