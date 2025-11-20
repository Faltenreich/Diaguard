package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.persistence.sqldelight.MeasurementPropertyQueries
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightExtensions.toSqlLiteLong
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertySqlDelightMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementPropertySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: MeasurementPropertySqlDelightMapper,
) : MeasurementPropertyDao, SqlDelightDao<MeasurementPropertyQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementPropertyQueries {
        return api.measurementPropertyQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementProperty?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        categoryId: Long,
        unitId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            key = key?.key,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle.stableId.toLong(),
            valueRangeMinimum = range.minimum,
            valueRangeLow = range.low,
            valueRangeTarget = range.target,
            valueRangeHigh = range.high,
            valueRangeMaximum = range.maximum,
            isValueRangeHighlighted = range.isHighlighted.toSqlLiteLong(),
            categoryId = categoryId,
            unitId = unitId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementProperty.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementProperty.Local?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun observeByKey(
        key: DatabaseKey.MeasurementProperty,
    ): Flow<MeasurementProperty.Local?> {
        return queries.getByKey(key.key, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local> {
        return queries.getByCategoryId(categoryId, mapper::map).executeAsList()
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>> {
        return queries.getByCategoryId(categoryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByCategoryKey(
        categoryKey: DatabaseKey.MeasurementCategory,
    ): Flow<List<MeasurementProperty.Local>> {
        return queries.getByCategoryKey(categoryKey.key, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeIfCategoryIsActive(
        excludedPropertyKey: DatabaseKey.MeasurementProperty,
    ): Flow<List<MeasurementProperty.Local>> {
        return queries.getIfCategoryIsActive(
            excludedPropertyKey = excludedPropertyKey.key,
            mapper = mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun getAll(): List<MeasurementProperty.Local> {
        return queries.getAll(mapper::map).executeAsList()
    }

    override fun observeAll(): Flow<List<MeasurementProperty.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getMaximumSortIndex(categoryId: Long): Long? {
        return queries.getMaximumSortIndex(categoryId).executeAsOneOrNull()?.MAX
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        unitId: Long,
    ) {
        queries.update(
            id = id,
            updatedAt = updatedAt.isoString,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle.stableId.toLong(),
            valueRangeMinimum = range.minimum,
            valueRangeLow = range.low,
            valueRangeTarget = range.target,
            valueRangeHigh = range.high,
            valueRangeMaximum = range.maximum,
            isValueRangeHighlighted = range.isHighlighted.toSqlLiteLong(),
            unitId = unitId,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}