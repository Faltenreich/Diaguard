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
    ): MeasurementProperty {
        val now = dateTimeFactory.now()
        // We set this temporary id because the corresponding unit will be created afterwards
        val selectedUnitId = MeasurementProperty.SELECTED_UNIT_ID_INVALID
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
        val id = checkNotNull(dao.getLastId())
        return MeasurementProperty(
            id = id,
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
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
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
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