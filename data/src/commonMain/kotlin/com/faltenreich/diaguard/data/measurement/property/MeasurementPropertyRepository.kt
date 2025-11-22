package com.faltenreich.diaguard.data.measurement.property

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.data.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository internal constructor(
    private val dao: MeasurementPropertyDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(property: MeasurementProperty.Seed): Long? = with(property) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            categoryId = category.id,
            unitId = unit.id,
        )
        return dao.getLastId()
    }

    fun create(property: MeasurementProperty.User): Long? = with(property) {
        val unit = unit ?: return@with null
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = null,
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            categoryId = category.id,
            unitId = unit.id,
        )
        dao.getLastId()
    }

    fun getById(id: Long): MeasurementProperty.Local? {
        return dao.getById(id)
    }

    fun observeByKey(key: DatabaseKey.MeasurementProperty): Flow<MeasurementProperty.Local?> {
        return dao.observeByKey(key)
    }

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local> {
        return dao.getByCategoryId(categoryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun observeByCategoryKey(
        categoryKey: DatabaseKey.MeasurementCategory,
    ): Flow<List<MeasurementProperty.Local>> {
        return dao.observeByCategoryKey(categoryKey)
    }

    fun observeIfCategoryIsActive(
        excludedPropertyKey: DatabaseKey.MeasurementProperty,
    ): Flow<List<MeasurementProperty.Local>> {
        return dao.observeIfCategoryIsActive(excludedPropertyKey)
    }

    fun getAll(): List<MeasurementProperty.Local> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementProperty.Local>> {
        return dao.observeAll()
    }

    fun getMaximumSortIndex(categoryId: Long): Long? {
        return dao.getMaximumSortIndex(categoryId)
    }

    fun update(property: MeasurementProperty.Local) = with(property) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            unitId = unit.id,
        )
    }

    fun delete(property: MeasurementProperty.Local) {
        dao.deleteById(property.id)
    }
}