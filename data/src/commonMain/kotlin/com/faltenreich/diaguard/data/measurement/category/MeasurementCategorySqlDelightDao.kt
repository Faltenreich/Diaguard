package com.faltenreich.diaguard.data.measurement.category

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.persistence.sqldelight.MeasurementCategoryQueries
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDao
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightExtensions.toSqlLiteLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementCategorySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MeasurementCategorySqlDelightMapper,
) : MeasurementCategoryDao, SqlDelightDao<MeasurementCategoryQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementCategoryQueries {
        return api.measurementCategoryQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            key = key?.key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive.toSqlLiteLong(),
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementCategory.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementCategory.Local?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeByKey(key: String): Flow<MeasurementCategory.Local?> {
        return queries.getByKey(key, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeActive(): Flow<List<MeasurementCategory.Local>> {
        return queries.getActive(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeAll(): Flow<List<MeasurementCategory.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun countAll(): Flow<Long> {
        return queries.countAll().asFlow().mapToOne(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            id = id,
            isActive = isActive.toSqlLiteLong(),
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}