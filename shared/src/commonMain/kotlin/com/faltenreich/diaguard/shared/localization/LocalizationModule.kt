package com.faltenreich.diaguard.shared.localization

import org.koin.dsl.module

fun localizationModule() = module {
    single<Localization> { ResourceLocalization() }
}