package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementPropertyQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertySqlDelightMapper
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

    override fun getAll(): Flow<List<MeasurementProperty>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun create(
        createdAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            name = name,
            icon = icon,
            sort_index = sortIndex,
        )
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            name = name,
            icon = icon,
            sort_index = sortIndex,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}