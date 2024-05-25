package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.form.CreateEntryUseCase
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementCategoryInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.entry.form.validation.RealisticMeasurementValueRule
import com.faltenreich.diaguard.entry.form.validation.ValidEntryFormInputRule
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.entry.search.SearchEntriesUseCase
import com.faltenreich.diaguard.entry.tag.CreateEntryTagsUseCase
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.Food
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun entryModule() = module {
    singleOf(::EntryRepository)
    singleOf(::EntryTagRepository)

    singleOf(::GetDateTimeForEntryUseCase)
    singleOf(::GetMeasurementCategoryInputStateUseCase)
    singleOf(::GetFoodEatenInputStateUseCase)
    singleOf(::GetTagsByQueryUseCase)
    singleOf(::GetTagsOfEntry)
    single {
        ValidateEntryFormInputUseCase(
            dispatcher = get(),
            ruleForEntryFormInput = ValidEntryFormInputRule(),
            rulesForCategories = listOf(),
            rulesForProperties = listOf(RealisticMeasurementValueRule()),
        )
    }
    singleOf(::CreateEntryUseCase)
    singleOf(::CreateEntryTagsUseCase)
    singleOf(::DeleteEntryUseCase)
    singleOf(::SearchEntriesUseCase)

    factory { (entry: Entry.Local?, date: Date?, food: Food.Local?) -> EntryFormViewModel(entry, date, food) }
    factory { (query: String) -> EntrySearchViewModel(query) }
}