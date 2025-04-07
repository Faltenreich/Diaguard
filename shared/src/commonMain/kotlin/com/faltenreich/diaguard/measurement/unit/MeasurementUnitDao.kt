package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementUnit?,
        name: String,
        abbreviation: String,
        factor: Double,
        isSelected: Boolean,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementUnit.Local?

    fun observeById(id: Long): Flow<MeasurementUnit.Local?>

    fun observeAll(): Flow<List<MeasurementUnit.Local>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
        isSelected: Boolean,
    )

    fun deleteById(id: Long)
}