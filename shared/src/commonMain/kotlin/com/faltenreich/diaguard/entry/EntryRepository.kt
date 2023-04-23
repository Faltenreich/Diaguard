package com.faltenreich.diaguard.entry

import org.koin.core.annotation.Single

@Single
class EntryRepository(
    private val dao: EntryDao,
) {

    suspend fun getAll(): List<Entry> {
        return dao.getAll()
    }

    suspend fun insert(entry: Entry) {
        dao.insert(entry)
    }
}