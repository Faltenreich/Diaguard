package com.faltenreich.diaguard.persistence.sqlite

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun sqliteModule(): Module = module {
    factory { SqliteDatabase(androidContext().getDatabasePath("diaguard.db")) }
}