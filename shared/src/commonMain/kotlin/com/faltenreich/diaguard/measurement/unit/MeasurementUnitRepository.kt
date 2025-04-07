package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(unit: MeasurementUnit.Seed): Long = with(unit) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            isSelected = isSelected,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(unit: MeasurementUnit.User): Long = with(unit) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = null,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            isSelected = isSelected,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementUnit.Local? {
        return dao.getById(id)
    }

    fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        return dao.observeAll()
    }

    fun update(unit: MeasurementUnit.Local) = with(unit) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            abbreviation = abbreviation,
            isSelected = isSelected,
        )
    }

    fun delete(unit: MeasurementUnit.Local) {
        dao.deleteById(unit.id)
    }
}