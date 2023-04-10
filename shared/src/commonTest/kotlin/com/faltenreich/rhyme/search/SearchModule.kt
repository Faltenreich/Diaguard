package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.search.api.SearchApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun searchModule() = module {
    single<SearchApi> { FakeSearchApi() }
    singleOf(::SearchRepository)
    singleOf(::SearchUseCase)
    singleOf(::SearchViewModel)
}