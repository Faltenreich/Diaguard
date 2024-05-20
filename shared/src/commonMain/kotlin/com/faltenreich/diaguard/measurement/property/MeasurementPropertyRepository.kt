package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(property: MeasurementProperty.User): Long = with(property) {
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
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(property: MeasurementProperty.Seed): Long = with(property) {
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

    fun update(property: MeasurementProperty.Local) = with(property) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            range = range,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
        )
    }

    fun delete(property: MeasurementProperty.Local) {
        dao.deleteById(property.id)
    }
}