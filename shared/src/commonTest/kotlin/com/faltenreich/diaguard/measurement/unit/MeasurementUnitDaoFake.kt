package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementUnitDaoFake : MeasurementUnitDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementUnit?,
        name: String,
        abbreviation: String,
        factor: Double,
        isSelected: Boolean,
        propertyId: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun observeById(id: Long): Flow<MeasurementUnit.Local?> {
        TODO("Not yet implemented")
    }

    override fun getByKey(key: String): MeasurementUnit.Local? {
        TODO("Not yet implemented")
    }

    override fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        TODO("Not yet implemented")
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
        isSelected: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}