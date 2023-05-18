package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.measurement.MeasurementProperty
import com.faltenreich.diaguard.measurement.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.MeasurementUnit
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementPropertyQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementPropertySqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementPropertySqlDelightMapper = inject(),
) : MeasurementPropertyDao, SqlDelightDao<MeasurementPropertyQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementPropertyQueries {
        return api.measurementPropertyQueries
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Flow<MeasurementProperty?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getAll(): Flow<List<MeasurementProperty>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun create(
        createdAt: DateTime,
        name: String,
        sortIndex: Long,
        selectedUnit: MeasurementUnit,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            name = name,
            sort_index = sortIndex,
            selected_unit_id = selectedUnit.id,
        )
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