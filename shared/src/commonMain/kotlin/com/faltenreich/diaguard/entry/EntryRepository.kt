package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import org.koin.core.annotation.Single

@Single
class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeRepository: DateTimeRepository,
) {

    fun getAll(): List<Entry> {
        return dao.getAll()
    }

    fun insert(entry: Entry) {
        dao.insert(entry)
    }

    fun create(): Entry {
        return Entry(
            id = 0,
            dateTime = dateTimeRepository.now(),
            note = null,
        )
    }
}