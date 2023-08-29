package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementUnitQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementUnitSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementUnitSqlDelightMapper = inject(),
) : MeasurementUnitDao, SqlDelightDao<MeasurementUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementUnitQueries {
        return api.measurementUnitQueries
    }

    override fun create(
        createdAt: DateTime,
        name: String,
        factor: Double,
        sortIndex: Long,
        typeId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            name = name,
            factor = factor,
            sort_index = sortIndex,
            type_id = typeId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Flow<MeasurementUnit?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByTypeId(typeId: Long): Flow<List<MeasurementUnit>> {
        return queries.getByType(typeId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeAll(): Flow<List<MeasurementUnit>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        factor: Double,
        sortIndex: Long,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            name = name,
            factor = factor,
            sort_index = sortIndex,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}