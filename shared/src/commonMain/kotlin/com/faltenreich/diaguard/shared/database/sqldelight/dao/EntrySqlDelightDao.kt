package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val dateTimeApi: DateTimeApi,
) : EntryDao, SqlDelightDao<EntryQueries> {

    override fun getQueries(api: SqlDelightApi): EntryQueries {
        return api.entryQueries
    }

    override fun getAll(): Flow<List<Entry>> {
        return queries.selectAll { id, dateTime, note ->
            Entry(
                id = id,
                dateTime = dateTimeApi.isoStringToDateTime(dateTime),
                note = note,
            )
        }.asFlow().mapToList(dispatcher)
    }

    override fun insert(entry: Entry) {
        queries.insert(
            dateTime = dateTimeApi.dateTimeToIsoString(entry.dateTime),
            note = entry.note,
        )
    }

    override fun delete(entry: Entry) {
        val entryId = entry.id ?: return
        queries.delete(id = entryId)
    }
}