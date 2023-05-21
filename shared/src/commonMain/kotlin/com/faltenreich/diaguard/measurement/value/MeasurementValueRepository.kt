package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
) {

    fun create(
        value: Double,
        typeId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByEntryId(entryId: Long): Flow<List<MeasurementValue>> {
        return dao.getByEntryId(entryId)
    }

    fun update(
        id: Long,
        value: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            value = value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}