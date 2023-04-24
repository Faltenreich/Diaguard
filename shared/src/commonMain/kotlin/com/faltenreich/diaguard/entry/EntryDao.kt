package com.faltenreich.diaguard.entry

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.shared.database.Database
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class EntryDao(
    private val dispatcher: CoroutineDispatcher,
    private val database: Database,
    // TODO: Remove when dao has been encapsulated better
    private val dateTimeRepository: DateTimeRepository,
) {

    fun getAll(): Flow<List<Entry>> {
        return database.sqlDelightDatabase.database.entryQueries.selectAll { id, date_time, note ->
            Entry(id, dateTimeRepository.convertIsoStringToDateTime(date_time), note)
        }.asFlow().mapToList(dispatcher)
    }

    fun insert(entry: Entry) {
        database.sqlDelightDatabase.database.entryQueries.insert(dateTimeRepository.convertDateTimeToIsoString(entry.dateTime), entry.note)
    }

    fun delete(entry: Entry) {
        val entryId = entry.id ?: return
        database.sqlDelightDatabase.database.entryQueries.delete(entryId)
    }
}