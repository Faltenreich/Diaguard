package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementCategoryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteLong
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementCategorySqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementCategorySqlDelightMapper = inject(),
) : MeasurementCategoryDao, SqlDelightDao<MeasurementCategoryQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementCategoryQueries {
        return api.measurementCategoryQueries
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementCategory? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementCategory?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeByKey(key: String): Flow<MeasurementCategory?> {
        return queries.getByKey(key, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeActive(): Flow<List<MeasurementCategory>> {
        return queries.getActive(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeAll(): Flow<List<MeasurementCategory>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun countAll(): Flow<Long> {
        return queries.countAll().asFlow().mapToOne(dispatcher)
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: String?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            key = key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive.toSqlLiteLong(),
        )
    }

    override fun update(category: MeasurementCategory) = with(category) {
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