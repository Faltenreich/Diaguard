package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
) {

    fun create(
        name: String,
        icon: String? = null,
        sortIndex: Long? = null,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            icon = icon,
            sortIndex = sortIndex ?: 0, // TODO: Current max + 1
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getById(id: Long): Flow<MeasurementProperty?> {
        return dao.getById(id)
    }

    fun getAll(): Flow<List<MeasurementProperty>> {
        return dao.getAll()
    }

    fun update(
        id: Long,
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}