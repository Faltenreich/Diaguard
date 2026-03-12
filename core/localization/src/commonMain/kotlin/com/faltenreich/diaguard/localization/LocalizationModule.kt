package com.faltenreich.diaguard.localization

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.config.BuildConfig
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun localizationModule() = module {
    factory { Locale.current }
    single<Localization> {
        if (get<BuildConfig>().hasPlatformFramework()) ComposeLocalization()
        else FakeLocalization()
    }
    factoryOf(::NumberFormatter)
}