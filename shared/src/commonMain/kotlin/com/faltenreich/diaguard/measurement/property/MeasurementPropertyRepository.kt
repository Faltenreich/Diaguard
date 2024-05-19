package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: DatabaseKey.MeasurementProperty?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        categoryId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            categoryId = categoryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementProperty.Local? {
        return dao.getById(id)
    }

    fun getByKey(key: String): MeasurementProperty.Local {
        return checkNotNull(dao.getByKey(key))
    }

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local> {
        return dao.getByCategoryId(categoryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun getAll(): List<MeasurementProperty.Local> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementProperty.Local>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}