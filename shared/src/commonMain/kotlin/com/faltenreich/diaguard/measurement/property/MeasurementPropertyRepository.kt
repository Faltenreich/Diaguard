package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
) {

    fun create(
        name: String,
        icon: String? = null,
        sortIndex: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getById(id: Long): MeasurementProperty? {
        return dao.getById(id)
    }

    fun observeById(id: Long): Flow<MeasurementProperty?> {
        return dao.observeById(id)
    }

    fun getAll(): List<MeasurementProperty> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementProperty>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    fun update(property: MeasurementProperty) = with(property) {
        update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}