package com.faltenreich.diaguard.import

import com.faltenreich.diaguard.import.legacy.LegacyImport
import com.faltenreich.diaguard.import.seed.SeedImport
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun importModule() = module {
    singleOf(::SeedImport)
    singleOf(::LegacyImport)
    singleOf(::ImportUseCase)
}