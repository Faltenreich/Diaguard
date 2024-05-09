package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.tag.detail.GetEntriesOfTagUseCase
import com.faltenreich.diaguard.tag.detail.TagDetailViewModel
import com.faltenreich.diaguard.tag.form.CreateTagUseCase
import com.faltenreich.diaguard.tag.form.DeleteTagUseCase
import com.faltenreich.diaguard.tag.form.HasTagUseCase
import com.faltenreich.diaguard.tag.form.TagFormViewModel
import com.faltenreich.diaguard.tag.form.UniqueTagRule
import com.faltenreich.diaguard.tag.form.ValidateTagUseCase
import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import com.faltenreich.diaguard.tag.list.TagListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun tagModule() = module {
    singleOf(::TagRepository)
    singleOf(::EntryTagRepository)

    singleOf(::GetTagsUseCase)
    singleOf(::HasTagUseCase)
    singleOf(::CreateTagUseCase)
    single { ValidateTagUseCase(listOf(UniqueTagRule())) }
    singleOf(::GetEntriesOfTagUseCase)
    singleOf(::DeleteTagUseCase)
    singleOf(::UpdateTagUseCase)
    singleOf(::CreateEntryTagsUseCase)

    factoryOf(::TagListViewModel)
    factoryOf(::TagFormViewModel)
    factory { (tag: Tag) -> TagDetailViewModel(tag = tag) }
}