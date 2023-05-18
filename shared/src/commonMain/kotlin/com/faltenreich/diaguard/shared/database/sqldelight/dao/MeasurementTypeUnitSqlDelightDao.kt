package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementTypeUnitQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementTypeUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementTypeUnitSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementTypeUnitSqlDelightMapper = inject(),
) : MeasurementTypeUnitDao, SqlDelightDao<MeasurementTypeUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementTypeUnitQueries {
        return api.measurementTypeUnitQueries
    }

    override fun create(
        createdAt: DateTime,
        factor: Double,
        typeId: Long,
        unitId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            factor = factor,
            type_id = typeId,
            unit_id = unitId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByTypeId(typeId: Long): Flow<List<MeasurementTypeUnit>> {
        return queries.getByType(typeId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        factor: Double,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            factor = factor,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}