package com.faltenreich.diaguard.language

import com.faltenreich.diaguard.language.source.LanguageDataSource
import com.faltenreich.diaguard.language.source.LanguageInMemoryDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun languageModule() = module {
    singleOf(::LanguageInMemoryDataSource) bind LanguageDataSource::class
    singleOf(::LanguageRepository)
    singleOf(::LanguageViewModel)
}