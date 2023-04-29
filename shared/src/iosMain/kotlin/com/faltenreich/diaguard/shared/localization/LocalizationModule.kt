package com.faltenreich.diaguard.shared.localization

import org.koin.dsl.bind
import org.koin.dsl.module

actual fun localizationModule() = module {
    single { PlatformLocalization() } bind Localization::class
}