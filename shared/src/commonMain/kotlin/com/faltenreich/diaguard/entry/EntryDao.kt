package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.database.Database
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import org.koin.core.annotation.Single

@Single
class EntryDao(
    private val database: Database,
    // TODO: Remove when dao has been encapsulated better
    private val dateTimeRepository: DateTimeRepository,
) {

    suspend fun getAll(): List<Entry> {
        return database.sqlDelightDatabase.database.entryQueries.selectAll { id, date_time, note ->
            Entry(id, dateTimeRepository.convertIsoStringToDateTime(date_time), note)
        }.executeAsList()
    }

    suspend fun insert(entry: Entry) {
        database.sqlDelightDatabase.database.entryQueries.insert(dateTimeRepository.convertDateTimeToIsoString(entry.dateTime), entry.note)
    }
}