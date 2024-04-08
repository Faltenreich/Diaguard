package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementValueDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        typeId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun observeByEntryId(entryId: Long): Flow<List<MeasurementValue>>

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue>>

    fun observeLatestByCategoryId(categoryId: Long): Flow<MeasurementValue?>

    fun getByEntryId(entryId: Long): List<MeasurementValue>

    fun observeByCategoryId(categoryId: Long): Flow<Long>

    fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ) : Flow<List<MeasurementValue>>

    fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?>

    fun getAverageByTypeId(
        typeId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double?

    fun observeCountByTypeId(typeId: Long): Flow<Long>

    fun countByCategoryId(categoryId: Long): Long

    fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    )

    fun deleteById(id: Long)
}