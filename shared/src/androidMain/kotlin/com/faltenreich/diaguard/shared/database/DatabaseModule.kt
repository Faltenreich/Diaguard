package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun databaseModule() = module {
    single { SqlDelightDriverFactory(androidContext()) }
}