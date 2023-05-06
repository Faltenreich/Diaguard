package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.GetEntryUseCase
import com.faltenreich.diaguard.entry.form.SubmitEntryUseCase
import com.faltenreich.diaguard.entry.search.SearchEntriesUseCase
import com.faltenreich.diaguard.log.LogViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun entryModule() = module {
    singleOf(::EntryRepository)

    singleOf(::LogViewModel)

    singleOf(::SearchEntriesUseCase)

    singleOf(::GetEntryUseCase)
    singleOf(::SubmitEntryUseCase)
    single { (entry: Entry?) -> EntryFormViewModel(entry = entry) }
}