package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementValueQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeasurementValueSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementValueSqlDelightMapper = inject(),
) : MeasurementValueDao, SqlDelightDao<MeasurementValueQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementValueQueries {
        return api.measurementValueQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        typeId: Long,
        entryId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            value_ = value,
            typeId = typeId,
            entryId = entryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByEntryId(entryId: Long): Flow<List<MeasurementValue>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime
    ): Flow<List<MeasurementValue>> {
        return queries.getByDateRange(
            startDateTime = startDateTime.isoString,
            endDateTime = endDateTime.isoString,
            mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun observeLatestByPropertyId(propertyId: Long): Flow<MeasurementValue?> {
        return queries.getLatestByProperty(propertyId, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByEntryId(entryId: Long): List<MeasurementValue> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
    }

    override fun observeByPropertyId(propertyId: Long): Flow<Long> {
        return queries.countByProperty(propertyId).asFlow().mapToOne(dispatcher)
    }

    override fun observeByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue>> {
        return queries.getPropertyAndDateTime(
            propertyId,
            minDateTime.isoString,
            maxDateTime.isoString,
            mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun observeAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        return queries.getAverageByProperty(
            propertyId,
            minDateTime.isoString,
            maxDateTime.isoString,
        ).asFlow().mapToOneOrNull(dispatcher).map { it?.AVG }
    }

    override fun getAverageByTypeId(
        typeId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        return queries.getAverageByProperty(
            typeId,
            minDateTime.isoString,
            maxDateTime.isoString,
        ).executeAsOneOrNull()?.AVG
    }

    override fun observeCountByTypeId(typeId: Long): Flow<Long> {
        return queries.countByType(typeId).asFlow().mapToOne(dispatcher)
    }

    override fun countByPropertyId(propertyId: Long): Long {
        return queries.countByProperty(propertyId).executeAsOne()
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            value_ = value,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}