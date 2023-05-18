package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementPropertyDao {

    fun create(
        createdAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    )

    fun getLastId(): Long?

    fun getAll(): Flow<List<MeasurementProperty>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    )

    fun deleteById(id: Long)
}