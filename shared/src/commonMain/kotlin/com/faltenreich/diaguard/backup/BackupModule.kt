package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedFactory
import com.faltenreich.diaguard.backup.seed.SeedImport
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun backupModule() = module {
    singleOf(::SeedFactory)
    singleOf(::SeedImport)
    singleOf(::LegacyImport)
    singleOf(::BackupUseCase)
}