package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.form.CreateEntryUseCase
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsInputDataUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.entry.search.SearchEntriesUseCase
import com.faltenreich.diaguard.shared.datetime.Date
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun entryModule() = module {
    singleOf(::EntryRepository)

    singleOf(::SearchEntriesUseCase)
    factory { (query: String) -> EntrySearchViewModel(query) }

    singleOf(::GetDateTimeForEntryUseCase)
    singleOf(::GetMeasurementsInputDataUseCase)
    singleOf(::GetFoodEatenInputDataUseCase)
    singleOf(::GetTagsByQueryUseCase)
    singleOf(::GetTagsOfEntry)
    singleOf(::CreateEntryUseCase)
    singleOf(::DeleteEntryUseCase)
    factory { (entry: Entry?, date: Date?) -> EntryFormViewModel(entry = entry, date = date) }
}