package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsFirstVisitUseCase(
    private val entryRepository: EntryRepository = inject(),
) {

    operator fun invoke(): Flow<Boolean> {
        return entryRepository.countAll().map { count -> count == 0L }
    }
}