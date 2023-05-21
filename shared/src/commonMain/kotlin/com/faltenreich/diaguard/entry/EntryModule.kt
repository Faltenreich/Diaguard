package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.SubmitEntryUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsUseCase
import com.faltenreich.diaguard.entry.list.EntryListViewModel
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.entry.search.SearchEntriesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun entryModule() = module {
    singleOf(::EntryRepository)

    singleOf(::EntryListViewModel)

    singleOf(::SearchEntriesUseCase)
    factory { (query: String?) -> EntrySearchViewModel(query) }

    singleOf(::GetMeasurementsUseCase)
    singleOf(::SubmitEntryUseCase)
    singleOf(::DeleteEntryUseCase)
    factory { (entry: Entry?) -> EntryFormViewModel(entry) }
}