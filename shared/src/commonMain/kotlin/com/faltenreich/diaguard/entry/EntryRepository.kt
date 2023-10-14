package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.deep
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(dateTime: DateTime): Long {
        dao.create(createdAt = dateTimeFactory.now(), dateTime = dateTime)
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry> {
        return dao.getByDateRange(startDateTime, endDateTime)
    }

    fun observeByDateRange(startDateTime: DateTime, endDateTime: DateTime): Flow<List<Entry>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(
        id: Long,
        dateTime: DateTime,
        note: String?,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun update(entry: Entry) {
        update(
            id = entry.id,
            dateTime = entry.dateTime,
            note = entry.note,
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
        val flows = entries.map { entry ->
            valueRepository.observeByEntryId(entry.id)
                .deep(entry)
                .map {  values ->
                    entry to values
                }
        }
        combine(flows) { entriesWithValues ->
            // FIXME: Sometimes not called
            entriesWithValues.map { (entry, values) ->
                entry.values = values
                entry
            }
        }
    }
}

fun List<Entry>.deep(
    valueRepository: MeasurementValueRepository = inject(),
): List<Entry> {
    return map { entry ->
        entry.apply {
            values = valueRepository.getByEntryId(entry.id).deep(entry = entry)
        }
    }
}