package com.faltenreich.diaguard.data.measurement.unit

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.SqlDelightDao
import com.faltenreich.diaguard.data.sqldelight.MeasurementUnitQueries
import com.faltenreich.diaguard.data.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class MeasurementUnitSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MeasurementUnitSqlDelightMapper,
) : MeasurementUnitDao, SqlDelightDao<MeasurementUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementUnitQueries {
        return api.measurementUnitQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementUnit?,
        name: String,
        abbreviation: String,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            key = key?.key,
            name = name,
            abbreviation = abbreviation,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementUnit.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementUnit.Local?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getByName(name: String): MeasurementUnit.Local? {
        return queries.getByName(name, mapper::map).executeAsOneOrNull()
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            name = name,
            abbreviation = abbreviation,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}