package com.faltenreich.diaguard.measurement.property

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MeasurementPropertyDaoFake(
    private val categoryDao: MeasurementCategoryDao = inject(),
) : MeasurementPropertyDao {

    private val cache = mutableStateListOf<MeasurementProperty.Local>()

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
        cache += MeasurementProperty.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            range = range,
            category = categoryDao.getById(categoryId)!!,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): MeasurementProperty.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun observeById(id: Long): Flow<MeasurementProperty.Local?> {
        return flowOf(cache.firstOrNull { it.id == id })
    }

    override fun getByKey(key: String): MeasurementProperty.Local? {
        return cache.firstOrNull { it.key?.key == key }
    }

    override fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local> {
        return cache.filter { it.category.id == categoryId }
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>> {
        return flowOf(cache.filter { it.category.id == categoryId })
    }

    override fun getAll(): List<MeasurementProperty.Local> {
        return cache
    }

    override fun observeAll(): Flow<List<MeasurementProperty.Local>> {
        return flowOf(cache)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            range = range,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}