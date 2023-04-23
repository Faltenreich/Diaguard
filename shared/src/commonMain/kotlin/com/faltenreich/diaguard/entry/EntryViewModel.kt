package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.di.inject
import org.koin.core.annotation.Single

@Single
class EntryViewModel(
    private val repository: EntryRepository = inject(),
) {

    fun getAll(): List<Entry> {
        return repository.getAll()
    }

    fun createEntry() {
        val entry = repository.create()
        repository.insert(entry)
    }
}