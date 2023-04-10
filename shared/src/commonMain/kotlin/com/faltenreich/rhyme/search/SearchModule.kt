package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.search.api.SearchApi
import com.faltenreich.rhyme.search.api.rhymebrain.RhymeBrainApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun searchModule() = module {
    single<SearchApi> { RhymeBrainApi() }
    singleOf(::SearchRepository)
    singleOf(::SearchUseCase)
    singleOf(::SearchViewModel)
}