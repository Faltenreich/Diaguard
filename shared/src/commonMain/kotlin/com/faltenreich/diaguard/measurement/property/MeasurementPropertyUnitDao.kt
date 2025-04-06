package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime

interface MeasurementPropertyUnitDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        factor: Double,
        propertyId: Long,
        unitId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementPropertyUnit.Local?

    fun deleteById(id: Long)
}