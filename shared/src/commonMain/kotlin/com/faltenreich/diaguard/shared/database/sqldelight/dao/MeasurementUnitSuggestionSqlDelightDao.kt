package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementUnitSuggestionQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi

class MeasurementUnitSuggestionSqlDelightDao : MeasurementUnitSuggestionDao, SqlDelightDao<MeasurementUnitSuggestionQueries> {

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
}