package com.faltenreich.diaguard.entry

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class EntryRepository(
    private val dao: EntryDao,
) {

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun insert(entry: Entry) {
        dao.insert(entry)
    }
}