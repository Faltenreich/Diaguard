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
        propertyId: Long,
    )

    fun getLastId(): Long?

    fun observeById(id: Long): Flow<MeasurementUnit.Local?>

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit.Local>>

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit.Local>>

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