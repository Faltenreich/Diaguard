package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementValueQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
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
        type: MeasurementType,
        measurement: Measurement,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            value_ = value,
            type_id = type.id,
            measurement_id = measurement.id,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByMeasurement(measurement: Measurement): Flow<List<MeasurementValue>> {
        return queries.getByMeasurement(measurement.id, mapper::map).asFlow().mapToList(dispatcher)
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