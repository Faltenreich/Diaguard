package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.form.GetEntryByIdUseCase
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.entry.form.StoreEntryUseCase
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementCategoryInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.reminder.GetReminderLabelUseCase
import com.faltenreich.diaguard.entry.form.reminder.GetReminderUseCase
import com.faltenreich.diaguard.entry.form.reminder.SetReminderUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagSuggestionsUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.entry.form.validation.RealisticMeasurementValueRule
import com.faltenreich.diaguard.entry.form.validation.ValidEntryFormInputRule
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.entry.search.SearchEntriesUseCase
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.entry.tag.StoreEntryTagsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun entryModule() = module {
    factoryOf(::EntryRepository)
    factoryOf(::EntryTagRepository)

    factoryOf(::GetEntryByIdUseCase)
    factoryOf(::GetFoodByIdUseCase)
    factoryOf(::GetDateTimeForEntryUseCase)
    factoryOf(::GetMeasurementCategoryInputStateUseCase)
    factoryOf(::GetFoodEatenInputStateUseCase)
    factoryOf(::GetTagSuggestionsUseCase)
    factoryOf(::GetTagsOfEntry)
    factoryOf(::MapEntryListItemStateUseCase)
    factoryOf(::SetReminderUseCase)
    factoryOf(::GetReminderUseCase)
    factoryOf(::GetReminderLabelUseCase)

    factory {
        ValidateEntryFormInputUseCase(
            dispatcher = get(),
            ruleForEntryFormInput = ValidEntryFormInputRule(),
            rulesForProperties = listOf(RealisticMeasurementValueRule()),
        )
    }
    factoryOf(::StoreEntryUseCase)
    factoryOf(::StoreEntryTagsUseCase)
    factoryOf(::DeleteEntryUseCase)
    factoryOf(::SearchEntriesUseCase)

    viewModel { (entryId: Long?, dateTimeIsoString: String?, foodId: Long?) ->
        EntryFormViewModel(entryId, dateTimeIsoString, foodId)
    }
    viewModel { (query: String) -> EntrySearchViewModel(query) }
}