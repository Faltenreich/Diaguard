package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
) {

    fun create(
        value: Double,
        typeId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByEntryId(entryId: Long): Flow<List<MeasurementValue>> {
        return dao.getByEntryId(entryId)
    }

    fun update(
        id: Long,
        value: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            value = value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun Flow<List<MeasurementValue>>.deep(
    entry: Entry,
    typeRepository: MeasurementTypeRepository = inject(),
): Flow<List<MeasurementValue>> {
    return flatMapLatest { values ->
        combine(
            values.map { value ->
                typeRepository.getById(value.typeId).filterNotNull().map { type ->
                    value to type
                }
            }
        ) {
            it.map { (value, type) ->
                value.entry = entry
                value.type = type
                value
            }
        }
    }
}