package com.faltenreich.diaguard.import

import com.faltenreich.diaguard.import.seed.SeedImport
import com.faltenreich.diaguard.shared.database.DatabaseLegacyImport
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun importModule() = module {
    singleOf(::SeedImport)
    singleOf(::DatabaseLegacyImport)
}