package com.faltenreich.diaguard.shared.database.sqldelight

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun sqlDelightModule() = module {
    single { SqlDelightDriverFactory(androidContext()) }
}