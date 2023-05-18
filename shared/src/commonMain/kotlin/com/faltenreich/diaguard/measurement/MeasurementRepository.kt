package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementRepository(
    private val dao: MeasurementDao,
) {

    fun create(
        type: MeasurementType,
        entry: Entry,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            type = type,
            entry = entry,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByEntry(entry: Entry): Flow<List<Measurement>> {
        return dao.getByEntry(entry)
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