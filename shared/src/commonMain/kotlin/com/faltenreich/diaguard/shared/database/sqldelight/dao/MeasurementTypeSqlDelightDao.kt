package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
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
        selectedUnitId: Long,
        propertyId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            name = name,
            sort_index = sortIndex,
            selected_unit_id = selectedUnitId,
            property_id = propertyId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementType?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementType>> {
        return queries.getByProperty(propertyId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getById(id: Long): MeasurementType? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun getByPropertyId(propertyId: Long): List<MeasurementType> {
        return queries.getByProperty(propertyId, mapper::map).executeAsList()
    }

    override fun observeAll(): Flow<List<MeasurementType>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            name = name,
            sort_index = sortIndex,
            selected_unit_id = selectedUnitId,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}