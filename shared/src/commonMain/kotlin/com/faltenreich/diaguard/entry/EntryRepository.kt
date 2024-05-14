package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.deep
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.deep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            dateTime = dateTime,
            note = note,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(dateTime: DateTime): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            dateTime = dateTime,
            note = null,
        )
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry> {
        return dao.getByDateRange(startDateTime, endDateTime)
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
    entryTagRepository: EntryTagRepository = inject(),
): Flow<List<Entry>> {
    return map { entries ->
        entries.map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(entry.id).deep(entry = entry)
                entryTags = entryTagRepository.getByEntryId(entry.id).deep(entry = entry)
            }
        }
    }
}

fun List<Entry>.deep(
    valueRepository: MeasurementValueRepository = inject(),
    entryTagRepository: EntryTagRepository = inject(),
): List<Entry> {
    return map { entry ->
        entry.apply {
            values = valueRepository.getByEntryId(entry.id).deep(entry = entry)
            entryTags = entryTagRepository.getByEntryId(entry.id).deep(entry = entry)
        }
    }
}