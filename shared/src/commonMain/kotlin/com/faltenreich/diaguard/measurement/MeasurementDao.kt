package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementDao {

    fun create(
        createdAt: DateTime,
        type: MeasurementType,
        entry: Entry,
    )

    fun getLastId(): Long?

    fun getByEntry(entry: Entry): Flow<List<Measurement>>

    fun update(
        id: Long,
        updatedAt: DateTime,
    )

    fun deleteById(id: Long)
}