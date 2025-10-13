package com.faltenreich.diaguard.shared.localization

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun localizationModule() = module {
    singleOf(::ComposeLocalization) bind Localization::class
    factoryOf(::NumberFormatter)
}