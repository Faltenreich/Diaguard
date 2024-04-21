package com.faltenreich.diaguard.shared.localization

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun localizationModule() = module {
    single<ResourceLocalization> { ComposeResourceLocalization() }
    singleOf(::Localization)
}