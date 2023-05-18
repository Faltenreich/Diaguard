package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
) {

    fun create(
        name: String,
        selectedUnit: MeasurementUnit,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            sortIndex = 0, // TODO: Current max + 1
            selectedUnit = selectedUnit,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getAll(): Flow<List<MeasurementProperty>> {
        return dao.getAll()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            name = name,
            sortIndex = sortIndex,
            selectedUnit = selectedUnit,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}