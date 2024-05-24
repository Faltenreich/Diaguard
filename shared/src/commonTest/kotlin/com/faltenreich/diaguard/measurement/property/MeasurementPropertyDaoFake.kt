package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementPropertyDaoFake : MeasurementPropertyDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementProperty?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        categoryId: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): MeasurementProperty.Local? {
        TODO("Not yet implemented")
    }

    override fun observeById(id: Long): Flow<MeasurementProperty.Local?> {
        TODO("Not yet implemented")
    }

    override fun getByKey(key: String): MeasurementProperty.Local? {
        TODO("Not yet implemented")
    }

    override fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local> {
        TODO("Not yet implemented")
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<MeasurementProperty.Local> {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<MeasurementProperty.Local>> {
        TODO("Not yet implemented")
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}