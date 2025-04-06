package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyUnit
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyUnitDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementPropertyUnitQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertyUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject

class MeasurementPropertyUnitSqlDelightDao(
    private val mapper: MeasurementPropertyUnitSqlDelightMapper = inject(),
) : MeasurementPropertyUnitDao, SqlDelightDao<MeasurementPropertyUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementPropertyUnitQueries {
        return api.measurementPropertyUnitQueries
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

    override fun getById(id: Long): MeasurementPropertyUnit.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}