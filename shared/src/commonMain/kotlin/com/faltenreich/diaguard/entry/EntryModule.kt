package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.list.EntryListViewModel
import com.faltenreich.diaguard.entry.search.EntrySearchUseCase
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun entryModule() = module {
    singleOf(::EntryRepository)

    singleOf(::EntryListViewModel)

    singleOf(::EntrySearchUseCase)
    singleOf(::EntrySearchViewModel)

    single { (entry: Entry?) -> EntryFormViewModel(entry = entry) }
}