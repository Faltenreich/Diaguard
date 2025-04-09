package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementUnitSuggestionQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSuggestionSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementUnitSuggestionSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementUnitSuggestionSqlDelightMapper = inject(),
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