package com.faltenreich.diaguard.shared.localization

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun nativeLocalizationModule() = module {
    singleOf(::NativeAndroidLocalization) bind NativeLocalization::class
}