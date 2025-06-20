package com.faltenreich.diaguard.shared.database.sqldelight

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun sqlDelightModule(inMemory: Boolean) = module {
    single<SqlDelightDriverFactory> {
        if (inMemory) SqlDelightInMemoryDriverFactory()
        else SqlDelightDiskDriverFactory(androidContext())
    }
}