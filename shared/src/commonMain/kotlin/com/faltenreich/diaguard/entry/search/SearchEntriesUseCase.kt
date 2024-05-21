package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.tag.EntryTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(query: String): Flow<List<Entry.Local>> {
        return entryRepository.getByQuery(query).map { entries ->
            entries.map { entry ->
                entry.apply {
                    values = valueRepository.getByEntryId(entry.id)
                    entryTags = entryTagRepository.getByEntryId(entry.id)
                }
            }
        }
    }
}