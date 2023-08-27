package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
) {

    fun create(
        name: String,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getById(id: Long): Flow<MeasurementUnit?> {
        return dao.getById(id)
    }

    fun observeAll(): Flow<List<MeasurementUnit>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
        )
    }

    fun update(unit: MeasurementUnit) = with(unit) {
        update(
            id = id,
            updatedAt = updatedAt,
            name = name,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}