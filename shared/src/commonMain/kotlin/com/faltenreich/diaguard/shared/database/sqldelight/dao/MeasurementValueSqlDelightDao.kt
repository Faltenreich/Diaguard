package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementValueQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementValueSqlDelightMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeasurementValueSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MeasurementValueSqlDelightMapper,
) : MeasurementValueDao, SqlDelightDao<MeasurementValueQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementValueQueries {
        return api.measurementValueQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        propertyId: Long,
        entryId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            value_ = value,
            propertyId = propertyId,
            entryId = entryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return queries.getByDateRange(
            startDateTime = startDateTime.isoString,
            endDateTime = endDateTime.isoString,
            mapper = mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun observeLatestByProperty(
        key: DatabaseKey.MeasurementProperty,
    ): Flow<MeasurementValue.Local?> {
        return queries.getLatestByProperty(
            key = key.key,
            mapper = mapper::map,
        ).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeByCategory(
        categoryKey: DatabaseKey.MeasurementCategory,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return queries.getByCategory(
            categoryKey = categoryKey.key,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
            mapper = mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
    }

    override fun observeCountByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Long> {
        return queries.countByProperty(
            propertyId = propertyId,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
        ).asFlow().mapToOne(dispatcher)
    }

    override fun observeCountByValueRange(
        range: ClosedRange<Double>,
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Long> {
        return queries.countByValueRange(
            propertyId = propertyId,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
            minimumValue = range.start,
            maximumValue = range.endInclusive,
        ).asFlow().mapToOne(dispatcher)
    }

    override fun observeAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?> {
        return queries.getAverageByPropertyId(
            propertyId = propertyId,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
        ).asFlow().mapToOneOrNull(dispatcher).map { it?.AVG }
    }

    override fun observeAverageByPropertyKey(
        propertyKey: DatabaseKey.MeasurementProperty,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?> {
        return queries.getAverageByPropertyKey(
            propertyKey = propertyKey.key,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
        ).asFlow().mapToOneOrNull(dispatcher).map { it?.AVG }
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