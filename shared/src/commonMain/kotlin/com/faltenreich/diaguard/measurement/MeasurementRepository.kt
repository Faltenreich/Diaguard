package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementRepository(
    private val dao: MeasurementDao,
) {

    fun create(
        typeId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            typeId = typeId,
            entryId = entryId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByEntryId(entryId: Long): Flow<List<Measurement>> {
        return dao.getByEntryId(entryId)
    }

    fun update(id: Long) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}