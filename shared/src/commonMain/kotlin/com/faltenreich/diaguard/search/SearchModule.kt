package com.faltenreich.diaguard.search

import com.faltenreich.diaguard.search.api.SearchApi
import com.faltenreich.diaguard.search.api.rhymebrain.RhymeBrainApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun searchModule() = module {
    single<SearchApi> { RhymeBrainApi() }
    singleOf(::SearchRepository)
    singleOf(::SearchUseCase)
    singleOf(::SearchViewModel)
}