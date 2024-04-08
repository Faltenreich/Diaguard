package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: String?,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        categoryId: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            // We set this temporary id because the corresponding unit will be created afterwards
            selectedUnitId = MeasurementType.SELECTED_UNIT_ID_INVALID,
            categoryId = categoryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementType? {
        return dao.getById(id)
    }

    fun getByKey(key: String): MeasurementType {
        return checkNotNull(dao.getByKey(key))
    }

    fun getByCategoryId(categoryId: Long): List<MeasurementType> {
        return dao.getByCategoryId(categoryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementType>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun getAll(): List<MeasurementType> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementType>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            range = range,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun MeasurementType.deep(
    categoryRepository: MeasurementCategoryRepository = inject(),
): MeasurementType {
    return apply {
        this.category = checkNotNull(categoryRepository.getById(categoryId))
        // TODO: Selected unit
    }
}