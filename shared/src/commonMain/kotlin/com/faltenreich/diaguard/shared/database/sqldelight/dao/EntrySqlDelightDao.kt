package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val database: SqlDelightDatabase,
    // TODO: Remove when dao has been encapsulated better
    private val dateTimeRepository: DateTimeRepository,
) : EntryDao {

    private val queries: EntryQueries
        get() = database.api.entryQueries

    override fun getAll(): Flow<List<Entry>> {
        return queries.selectAll { id, date_time, note ->
            Entry(id, dateTimeRepository.convertIsoStringToDateTime(date_time), note)
        }.asFlow().mapToList(dispatcher)
    }

    override fun insert(entry: Entry) {
        queries.insert(dateTimeRepository.convertDateTimeToIsoString(entry.dateTime), entry.note)
    }

    override fun delete(entry: Entry) {
        val entryId = entry.id ?: return
        queries.delete(entryId)
    }
}