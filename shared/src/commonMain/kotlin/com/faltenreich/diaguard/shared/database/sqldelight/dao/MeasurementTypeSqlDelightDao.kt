package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementTypeQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementTypeSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementTypeSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementTypeSqlDelightMapper = inject(),
) : MeasurementTypeDao, SqlDelightDao<MeasurementTypeQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementTypeQueries {
        return api.measurementTypeQueries
    }

    override fun create(
        createdAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
        property: MeasurementProperty,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            name = name,
            sort_index = sortIndex,
            selected_unit_id = selectedUnit.id,
            property_id = property.id,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByProperty(property: MeasurementProperty): Flow<List<MeasurementType>> {
        return queries.getByProperty(property.id, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            name = name,
            sort_index = sortIndex,
            selected_unit_id = selectedUnit.id,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}