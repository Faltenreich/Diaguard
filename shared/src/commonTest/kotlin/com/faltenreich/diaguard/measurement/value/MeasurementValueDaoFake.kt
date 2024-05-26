package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementValueDaoFake : MeasurementValueDao {

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

    override fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        TODO("Not yet implemented")
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeLatestByCategoryId(categoryId: Long): Flow<MeasurementValue.Local?> {
        TODO("Not yet implemented")
    }

    override fun observeByCategoryId(categoryId: Long): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        TODO("Not yet implemented")
    }

    override fun getAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        TODO("Not yet implemented")
    }

    override fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun countByCategoryId(categoryId: Long): Long {
        TODO("Not yet implemented")
    }

    override fun update(id: Long, updatedAt: DateTime, value: Double) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}