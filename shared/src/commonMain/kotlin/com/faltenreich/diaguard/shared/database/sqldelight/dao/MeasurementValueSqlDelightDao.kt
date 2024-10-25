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

    override fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
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

    override fun observeCountByCategoryId(categoryId: Long): Flow<Long> {
        return queries.countByCategory(categoryId).asFlow().mapToOne(dispatcher)
    }

    override fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return queries.getCategoryAndDateTime(
            categoryId = categoryId,
            minDateTime = minDateTime.isoString,
            maxDateTime= maxDateTime.isoString,
            mapper = mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?> {
        return queries.getAverageByCategory(
            categoryId = categoryId,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
        ).asFlow().mapToOneOrNull(dispatcher).map { it?.AVG }
    }

    override fun observeAverageByPropertyKey(
        propertyKey: DatabaseKey.MeasurementProperty,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<MeasurementValue.Average?> {
        // FIXME: Throws NullPointerException in unit test with JDBC driver
        return queries.getAverageByPropertyKey(
            propertyKey = propertyKey.key,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
            mapper = mapper::map,
        ).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Double? {
        return queries.getAverageByProperty(
            propertyId = propertyId,
            minDateTime = minDateTime.isoString,
            maxDateTime = maxDateTime.isoString,
        ).executeAsOneOrNull()?.AVG
    }

    override fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return queries.countByProperty(propertyId).asFlow().mapToOne(dispatcher)
    }

    override fun countByCategoryId(categoryId: Long): Long {
        return queries.countByCategory(categoryId).executeAsOne()
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