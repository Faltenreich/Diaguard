package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeApi: DateTimeApi,
) {

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun create(): Entry {
        dao.create(dateTimeApi.now())
        val id = dao.getLastId() ?: throw IllegalStateException("No entry found")
        return dao.getById(id) ?: throw IllegalStateException("No entry found")
    }

    fun update(entry: Entry) {
        dao.update(entry)
    }

    fun delete(entry: Entry) {
        dao.delete(entry)
    }

    fun search(query: String): Flow<List<Entry>> {
        return dao.getByQuery(query)
    }
}