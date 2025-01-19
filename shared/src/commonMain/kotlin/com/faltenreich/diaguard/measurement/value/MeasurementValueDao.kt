package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

interface MeasurementValueDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        propertyId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun getByEntryId(entryId: Long): List<MeasurementValue.Local>

    fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ) : Flow<List<MeasurementValue.Local>>

    fun observeByCategoryKeyAndDateRange(
        categoryKey: DatabaseKey.MeasurementCategory,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ) : Flow<List<MeasurementValue.Local>>

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>>

    fun observeLatestByProperty(key: DatabaseKey.MeasurementProperty): Flow<MeasurementValue.Local?>

    fun observeCountByPropertyId(propertyId: Long): Flow<Long>

    fun observeCountByCategoryId(categoryId: Long): Flow<Long>

    fun observeAverageByPropertyKey(
        propertyKey: DatabaseKey.MeasurementProperty,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?>

    fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?>

    fun observeAveragesByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Average>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    )

    fun deleteById(id: Long)
}