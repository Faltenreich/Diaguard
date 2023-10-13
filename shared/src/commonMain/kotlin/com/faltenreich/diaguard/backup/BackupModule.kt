package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.backup.seed.SeedMapper
import com.faltenreich.diaguard.backup.seed.SeedReader
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun backupModule() = module {
    single<SeedReader> {
        object : SeedReader {
            override fun invoke(): String {
                return inject<ResourceFileReader>().value.read(MR.files.properties)
            }
        }
    }
    singleOf(::SeedMapper)
    singleOf(::SeedImport)
    singleOf(::LegacyImport)
    singleOf(::BackupUseCase)
}