package com.faltenreich.rhyme.shared.localization

import org.koin.dsl.bind
import org.koin.dsl.module

fun localizationModule() = module {
    single { FakeLocalization() } bind Localization::class
}