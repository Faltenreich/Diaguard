package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementTypeDao {

    fun create(
        createdAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
        propertyId: Long,
    )

    fun getLastId(): Long?

    fun observeById(id: Long): Flow<MeasurementType?>

    fun getById(id: Long): MeasurementType?

    fun getByPropertyId(propertyId: Long): Flow<List<MeasurementType>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
    )

    fun deleteById(id: Long)
}