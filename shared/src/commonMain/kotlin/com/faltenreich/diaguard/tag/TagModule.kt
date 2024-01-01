package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.tag.form.CreateTagUseCase
import com.faltenreich.diaguard.tag.form.HasTagUseCase
import com.faltenreich.diaguard.tag.form.TagFormViewModel
import com.faltenreich.diaguard.tag.list.DeleteTagUseCase
import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import com.faltenreich.diaguard.tag.list.TagListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun tagModule() = module {
    singleOf(::TagRepository)

    singleOf(::GetTagsUseCase)
    singleOf(::HasTagUseCase)
    singleOf(::CreateTagUseCase)
    singleOf(::DeleteTagUseCase)

    singleOf(::TagListViewModel)
    singleOf(::TagFormViewModel)
}