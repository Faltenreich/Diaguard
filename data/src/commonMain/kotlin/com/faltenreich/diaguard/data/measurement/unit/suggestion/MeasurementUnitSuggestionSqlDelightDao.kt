package com.faltenreich.diaguard.data.measurement.unit.suggestion

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.persistence.sqldelight.MeasurementUnitSuggestionQueries
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementUnitSuggestionSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MeasurementUnitSuggestionSqlDelightMapper,
) : MeasurementUnitSuggestionDao, SqlDelightDao<MeasurementUnitSuggestionQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementUnitSuggestionQueries {
        return api.measurementUnitSuggestionQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        factor: Double,
        propertyId: Long,
        unitId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            factor = factor,
            propertyId = propertyId,
            unitId = unitId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByProperty(propertyId: Long): Flow<List<MeasurementUnitSuggestion.Local>> {
        return queries.getByProperty(propertyId, mapper::map).asFlow().mapToList(dispatcher)
    }
}