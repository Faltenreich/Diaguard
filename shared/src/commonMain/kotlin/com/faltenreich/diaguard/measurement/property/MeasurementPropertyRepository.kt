package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: String?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        categoryId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            // We set this temporary id because the corresponding unit will be created afterwards
            selectedUnitId = MeasurementProperty.SELECTED_UNIT_ID_INVALID,
            categoryId = categoryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementProperty? {
        return dao.getById(id)
    }

    fun getByKey(key: String): MeasurementProperty {
        return checkNotNull(dao.getByKey(key))
    }

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty> {
        return dao.getByCategoryId(categoryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun getAll(): List<MeasurementProperty> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementProperty>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            selectedUnitId = selectedUnitId,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun MeasurementProperty.deep(
    categoryRepository: MeasurementCategoryRepository = inject(),
): MeasurementProperty {
    return apply {
        this.category = checkNotNull(categoryRepository.getById(categoryId))
        // TODO: Selected unit
    }
}