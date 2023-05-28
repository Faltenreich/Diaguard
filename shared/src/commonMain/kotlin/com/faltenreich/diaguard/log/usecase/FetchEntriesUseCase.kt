package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression
import kotlinx.coroutines.flow.first

class FetchEntriesUseCase(
    private val entryRepository: EntryRepository,
) {

    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): Map<Date, List<Entry>> {
        return DateProgression(startDate, endDate).associateWith { date ->
            entryRepository.getByDate(date).first()
        }
    }
}