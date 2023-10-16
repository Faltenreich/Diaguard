package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        name: String,
        key: String?,
        icon: String?,
        sortIndex: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            name = name,
            key = key,
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

    fun countAll(): Flow<Long> {
        return dao.countAll()
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