package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun databaseModule() = module {
    singleOf(::SqlDelightDatabase)
    singleOf(::EntrySqlDelightMapper)
    single<EntryDao> { EntrySqlDelightDao() }
}