package com.faltenreich.diaguard.shared.database.sqlite

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun sqliteModule(): Module = module {
    single { SqliteDatabase(androidContext().getDatabasePath("diaguard.db")) }
}