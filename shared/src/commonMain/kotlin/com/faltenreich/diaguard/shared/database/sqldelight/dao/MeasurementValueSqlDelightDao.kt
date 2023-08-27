package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementValueQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementValueSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementValueSqlDelightMapper = inject(),
) : MeasurementValueDao, SqlDelightDao<MeasurementValueQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementValueQueries {
        return api.measurementValueQueries
    }

    override fun create(
        createdAt: DateTime,
        value: Double,
        typeId: Long,
        entryId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            value_ = value,
            type_id = typeId,
            entry_id = entryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByEntryId(entryId: Long): Flow<List<MeasurementValue>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeLatestByPropertyId(propertyId: Long): Flow<MeasurementValue?> {
        return queries.getLatestByProperty(propertyId, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByEntryId(entryId: Long): List<MeasurementValue> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
    }

    override fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return queries.countByProperty(propertyId).asFlow().mapToOne(dispatcher)
    }

    override fun observeCountByTypeId(typeId: Long): Flow<Long> {
        return queries.countByType(typeId).asFlow().mapToOne(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            value_ = value,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}