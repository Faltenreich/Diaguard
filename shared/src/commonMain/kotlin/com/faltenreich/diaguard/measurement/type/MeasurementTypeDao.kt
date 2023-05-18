package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementTypeDao {

    fun create(
        createdAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
        property: MeasurementProperty,
    )

    fun getLastId(): Long?

    fun getByProperty(property: MeasurementProperty): Flow<List<MeasurementType>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    )

    fun deleteById(id: Long)
}