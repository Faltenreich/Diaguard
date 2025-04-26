package com.faltenreich.diaguard.shared.localization

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun localizationModule() = module {
    single<Localization> { ComposeLocalization() }
    factoryOf(::NumberFormatter)
}