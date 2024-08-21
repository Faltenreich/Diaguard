package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(query: String): Flow<List<Entry.Localized>> {
        return entryRepository.getByQuery(query).map { entries ->
            entries.map { entry ->
                Entry.Localized(
                    id = entry.id,
                    createdAt = entry.createdAt,
                    updatedAt = entry.updatedAt,
                    dateTime = entry.dateTime,
                    note = entry.note,
                    dateTimeFormatted = dateTimeFormatter.formatDateTime(entry.dateTime),
                    dateFormatted = dateTimeFormatter.formatDate(entry.dateTime.date),
                    timeFormatted = dateTimeFormatter.formatTime(entry.dateTime.time),
                    values = valueRepository.getByEntryId(entry.id),
                    entryTags = entryTagRepository.getByEntryId(entry.id),
                    foodEaten = foodEatenRepository.getByEntryId(entry.id),
                )
            }
        }
    }
}