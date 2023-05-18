package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementDao {

    fun create(
        createdAt: DateTime,
        typeId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun getByEntryId(entryId: Long): Flow<List<Measurement>>

    fun update(
        id: Long,
        updatedAt: DateTime,
    )

    fun deleteById(id: Long)
}