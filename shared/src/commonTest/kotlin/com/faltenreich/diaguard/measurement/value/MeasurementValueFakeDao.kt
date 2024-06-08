package com.faltenreich.diaguard.measurement.value

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class MeasurementValueFakeDao(
    private val propertyDao: MeasurementPropertyDao = inject(),
    private val entryDao: EntryDao = inject(),
) : MeasurementValueDao {

    private val cache = mutableStateListOf<MeasurementValue.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        propertyId: Long,
        entryId: Long
    ) {
        cache += MeasurementValue.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            value = value,
            property = propertyDao.getById(propertyId)!!,
            entry = entryDao.getById(entryId)!!,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return cache.filter { it.entry.id == entryId }
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        return flowOf(
            cache.filter {
                it.entry.dateTime > startDateTime && it.entry.dateTime < endDateTime
            }
        )
    }

    override fun observeLatestByCategoryId(categoryId: Long): Flow<MeasurementValue.Local?> {
        return flowOf(cache.firstOrNull { it.property.category.id == categoryId })

    }

    override fun observeCountByCategoryId(categoryId: Long): Flow<Long> {
        return flowOf(cache.size.toLong())
    }

    override fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        return flowOf(cache.filter { it.property.category.id == categoryId })
    }

    override fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        return flowOf(
            cache
                .filter { it.property.category.id == categoryId }
                .takeIf(List<*>::isNotEmpty)
                ?.map { it.value }
                ?.average()
        )
    }

    override fun getAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        return cache
            .filter { it.property.id == propertyId }
            .takeIf(List<*>::isNotEmpty)
            ?.map { it.value }
            ?.average()
    }

    override fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return flowOf(cache.count { it.property.id == propertyId }.toLong())
    }

    override fun countByCategoryId(categoryId: Long): Long {
        return cache.count { it.property.category.id == categoryId }.toLong()
    }

    override fun update(id: Long, updatedAt: DateTime, value: Double) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            value = value,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}