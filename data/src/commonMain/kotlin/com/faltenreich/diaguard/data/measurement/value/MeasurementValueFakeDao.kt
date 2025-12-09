package com.faltenreich.diaguard.data.measurement.value

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementValueFakeDao(private val values: List<MeasurementValue>) : MeasurementValueDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        propertyId: Long,
        entryId: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
        propertyKey: DatabaseKey.MeasurementProperty
    ): Flow<List<MeasurementValue.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeByDateRangeIfCategoryIsActive(
        startDateTime: DateTime,
        endDateTime: DateTime,
        excludedPropertyKey: DatabaseKey.MeasurementProperty
    ): Flow<List<MeasurementValue.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeLatestByProperty(key: DatabaseKey.MeasurementProperty): Flow<MeasurementValue.Local?> {
        TODO("Not yet implemented")
    }

    override fun observeByCategory(
        categoryKey: DatabaseKey.MeasurementCategory,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        TODO("Not yet implemented")
    }

    override fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        TODO("Not yet implemented")
    }

    override fun observeCountByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun observeCountByValueRange(
        range: ClosedRange<Double>,
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun observeAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        TODO("Not yet implemented")
    }

    override fun observeAverageByPropertyKey(
        propertyKey: DatabaseKey.MeasurementProperty,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        TODO("Not yet implemented")
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }


}