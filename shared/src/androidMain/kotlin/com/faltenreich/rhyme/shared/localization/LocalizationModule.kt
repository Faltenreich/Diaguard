package com.faltenreich.rhyme.shared.localization

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun localizationModule() = module {
    single { PlatformLocalization(androidContext()) } bind Localization::class
}