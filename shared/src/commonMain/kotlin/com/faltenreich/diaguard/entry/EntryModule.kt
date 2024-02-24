package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.delete.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.delete.EntryDeleteViewModel
import com.faltenreich.diaguard.entry.form.CreateEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementPropertyInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.entry.form.validation.ExhaustiveMeasurementValuesRule
import com.faltenreich.diaguard.entry.form.validation.RealisticMeasurementValueRule
import com.faltenreich.diaguard.entry.form.validation.ValidEntryFormInputRule
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
    singleOf(::GetMeasurementPropertyInputStateUseCase)
    singleOf(::GetFoodEatenInputStateUseCase)
    singleOf(::GetTagsByQueryUseCase)
    singleOf(::GetTagsOfEntry)
    single {
        ValidateEntryFormInputUseCase(
            dispatcher = get(),
            ruleForEntryFormInput = ValidEntryFormInputRule(),
            rulesForProperties = listOf(ExhaustiveMeasurementValuesRule()),
            rulesForTypes = listOf(RealisticMeasurementValueRule()),
        )
    }
    singleOf(::CreateEntryUseCase)
    singleOf(::DeleteEntryUseCase)
    factory { (entry: Entry?, date: Date?) -> EntryFormViewModel(entry = entry, date = date) }
    factory { (entry: Entry) -> EntryDeleteViewModel(entry) }
}