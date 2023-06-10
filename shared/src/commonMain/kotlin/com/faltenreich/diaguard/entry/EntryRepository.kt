package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.deep
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class EntryRepository(
    private val dao: EntryDao,
) {

    fun create(dateTime: DateTime): Long {
        dao.create(createdAt = DateTime.now(), dateTime = dateTime)
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): Flow<List<Entry>> {
        return dao.getByDateRange(startDateTime, endDateTime)
    }

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
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
        return dao.getByQuery(query)
    }
}

fun Flow<List<Entry>>.deep(
    valueRepository: MeasurementValueRepository = inject(),
): Flow<List<Entry>> {
    return flatMapLatest { entries ->
        combine(
            entries.map { entry ->
                valueRepository.getByEntryId(entry.id).deep(entry).map {  values ->
                    entry to values
                }
            }
        ) {
            it.map { (entry, values) ->
                entry.values = values
                entry
            }
        }
    }
}