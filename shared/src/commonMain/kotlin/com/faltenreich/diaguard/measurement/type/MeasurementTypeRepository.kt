package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
) {

    fun create(
        name: String,
        selectedUnit: MeasurementUnit,
        property: MeasurementProperty,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            sortIndex = 0, // TODO: Current max + 1
            selectedUnit = selectedUnit,
            property = property,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByProperty(property: MeasurementProperty): Flow<List<MeasurementType>> {
        return dao.getByProperty(property)
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