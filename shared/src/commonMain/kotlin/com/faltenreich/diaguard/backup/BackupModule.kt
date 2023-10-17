package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedFactory
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun backupModule() = module {
    single<FileReader> { ResourceFileReader(MR.files.properties) }
    singleOf(::SeedFactory)
    singleOf(::SeedImport)
    singleOf(::LegacyImport)
    singleOf(::BackupUseCase)
}