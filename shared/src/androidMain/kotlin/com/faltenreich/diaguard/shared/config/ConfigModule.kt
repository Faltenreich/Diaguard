package com.faltenreich.diaguard.shared.config

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun configModule(): Module = module {
    single { BuildConfig(androidContext()) }
}