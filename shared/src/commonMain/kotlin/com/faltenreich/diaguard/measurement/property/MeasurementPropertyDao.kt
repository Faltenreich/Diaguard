package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementPropertyDao {

    fun create(
        createdAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Flow<MeasurementProperty?>

    fun getAll(): Flow<List<MeasurementProperty>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    )

    fun deleteById(id: Long)
}