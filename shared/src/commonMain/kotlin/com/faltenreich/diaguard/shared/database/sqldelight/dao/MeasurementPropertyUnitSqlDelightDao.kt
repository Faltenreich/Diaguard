package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyUnit
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyUnitDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementPropertyUnitQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher

class MeasurementPropertyUnitSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
) : MeasurementPropertyUnitDao, SqlDelightDao<MeasurementPropertyUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementPropertyUnitQueries {
        return api.measurementPropertyUnitQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        propertyId: Long,
        unitId: Long,
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): MeasurementPropertyUnit.Local? {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}