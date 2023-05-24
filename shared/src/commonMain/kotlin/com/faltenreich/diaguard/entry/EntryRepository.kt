package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class EntryRepository(
    private val dao: EntryDao,
    private val measurementValueRepository: MeasurementValueRepository,
) {

    private fun Flow<List<Entry>>.deep(): Flow<List<Entry>> {
        return map { entries ->
            entries.map { entry ->
                // TODO: Replace with flow with suspending function
                entry.apply {
                    values = measurementValueRepository.getByEntryId(entry.id).first()
                }
            }
        }
    }

    fun create(dateTime: DateTime): Long {
        dao.create(createdAt = DateTime.now(), dateTime = dateTime)
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByDate(date: Date): Flow<List<Entry>> {
        return dao.getByDateRange(
            startDateTime = date.atTime(Time.atStartOfDay()),
            endDateTime = date.atTime(Time.atEndOfDay()),
        )
    }

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll().deep()
    }

    fun update(
        id: Long,
        dateTime: DateTime,
        note: String?,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    fun search(query: String): Flow<List<Entry>> {
        return dao.getByQuery(query).deep()
    }
}