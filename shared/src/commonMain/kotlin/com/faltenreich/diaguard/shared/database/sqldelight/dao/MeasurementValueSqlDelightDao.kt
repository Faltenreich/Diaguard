package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
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
        measurementId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            value_ = value,
            type_id = typeId,
            measurement_id = measurementId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByMeasurementId(measurementId: Long): Flow<List<MeasurementValue>> {
        return queries.getByMeasurement(measurementId, mapper::map).asFlow().mapToList(dispatcher)
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