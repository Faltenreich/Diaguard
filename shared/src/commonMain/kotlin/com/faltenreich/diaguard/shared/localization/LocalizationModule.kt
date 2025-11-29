package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun localizationModule() = module {
    factory { Locale.current }
    singleOf(::ComposeLocalization) bind Localization::class
    factoryOf(::NumberFormatter)
}