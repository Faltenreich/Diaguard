package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementCategoryDaoFake : MeasurementCategoryDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): MeasurementCategory.Local? {
        TODO("Not yet implemented")
    }

    override fun observeById(id: Long): Flow<MeasurementCategory.Local?> {
        TODO("Not yet implemented")
    }

    override fun observeByKey(key: String): Flow<MeasurementCategory.Local?> {
        TODO("Not yet implemented")
    }

    override fun observeActive(): Flow<List<MeasurementCategory.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<MeasurementCategory.Local>> {
        TODO("Not yet implemented")
    }

    override fun countAll(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}