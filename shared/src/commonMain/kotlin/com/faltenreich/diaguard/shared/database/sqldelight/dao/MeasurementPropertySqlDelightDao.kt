package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementPropertyQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteLong
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertySqlDelightMapper
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

    override fun create(
        createdAt: DateTime,
        key: String?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        selectedUnitId: Long,
        categoryId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = createdAt.isoString,
            key = key,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle.stableId.toLong(),
            valueRangeMinimum = range.minimum,
            valueRangeLow = range.low,
            valueRangeTarget = range.target,
            valueRangeHigh = range.high,
            valueRangeMaximum = range.maximum,
            isValueRangeHighlighted = range.isHighlighted.toSqlLiteLong(),
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementProperty? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementProperty?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByKey(key: String): MeasurementProperty? {
        return queries.getByKey(key, mapper::map).executeAsOneOrNull()
    }

    override fun getByCategoryId(categoryId: Long): List<MeasurementProperty> {
        return queries.getByCategory(categoryId, mapper::map).executeAsList()
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty>> {
        return queries.getByCategory(categoryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getAll(): List<MeasurementProperty> {
        return queries.getAll(mapper::map).executeAsList()
    }

    override fun observeAll(): Flow<List<MeasurementProperty>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    ) {
        queries.update(
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
            selectedUnitId = selectedUnitId,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}