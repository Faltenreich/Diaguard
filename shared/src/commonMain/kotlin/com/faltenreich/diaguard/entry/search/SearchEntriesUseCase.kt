package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(query: String): Flow<List<Entry>> {
        return entryRepository.search(query).map { entries ->
            entries.map { entry ->
                entry.values = valueRepository.getByEntryId(entry.id).map { value ->
                    val type = typeRepository.getById(value.typeId)?.apply {
                        property = checkNotNull(propertyRepository.getById(propertyId))
                    }
                    value.type = checkNotNull(type)
                    value
                }
                entry
            }
        }
    }
}