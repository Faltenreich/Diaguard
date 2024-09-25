package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.tag.detail.GetEntriesOfTagUseCase
import com.faltenreich.diaguard.tag.detail.GetTagByIdUseCase
import com.faltenreich.diaguard.tag.detail.TagDetailViewModel
import com.faltenreich.diaguard.tag.detail.DeleteTagUseCase
import com.faltenreich.diaguard.tag.form.TagFormViewModel
import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import com.faltenreich.diaguard.tag.list.TagListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun tagModule() = module {
    singleOf(::TagRepository)

    singleOf(::GetTagByIdUseCase)
    singleOf(::GetTagsUseCase)
    singleOf(::StoreTagUseCase)
    single { ValidateTagUseCase(rules = listOf(UniqueTagRule())) }
    singleOf(::GetEntriesOfTagUseCase)
    singleOf(::DeleteTagUseCase)

    viewModelOf(::TagListViewModel)
    viewModelOf(::TagFormViewModel)
    viewModel { (tagId: Long) -> TagDetailViewModel(tagId = tagId) }
}