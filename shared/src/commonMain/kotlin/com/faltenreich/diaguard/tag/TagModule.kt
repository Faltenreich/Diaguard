package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import com.faltenreich.diaguard.tag.list.TagListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun tagModule() = module {
    singleOf(::TagRepository)

    singleOf(::GetTagsUseCase)

    singleOf(::TagListViewModel)
}