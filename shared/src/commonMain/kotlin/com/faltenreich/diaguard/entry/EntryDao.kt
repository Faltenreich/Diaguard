package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.database.Database
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import org.koin.core.annotation.Single

@Single
class EntryDao(
    private val database: Database,
    private val dateTimeRepository: DateTimeRepository,
) {

    fun getAll(): List<Entry> {
        return database.sqlDelightDatabase.database.entryQueries.selectAll { id, date_time, note ->
            Entry(id, dateTimeRepository.createDateTimeFromIsoString(date_time), note)
        }.executeAsList()
    }
}