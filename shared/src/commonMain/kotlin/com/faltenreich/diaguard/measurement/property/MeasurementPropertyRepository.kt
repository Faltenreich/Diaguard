package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyRepository(
    private val dao: MeasurementPropertyDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: String?,
        name: String,
        icon: String?,
        sortIndex: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            key = key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementProperty? {
        return dao.getById(id)
    }

    fun observeById(id: Long): Flow<MeasurementProperty?> {
        return dao.observeById(id)
    }

    fun getByKey(key: DatabaseKey.MeasurementProperty): MeasurementProperty {
        return checkNotNull(dao.getByKey(key.key))
    }

    fun getBloodSugar(): MeasurementProperty {
        return getByKey(DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
    }

    fun getAll(): List<MeasurementProperty> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementProperty>> {
        return dao.observeAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    fun update(property: MeasurementProperty) = with(property) {
        update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}