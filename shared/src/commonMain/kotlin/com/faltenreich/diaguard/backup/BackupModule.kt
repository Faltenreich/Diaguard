package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.legacy.legacyModule
import com.faltenreich.diaguard.backup.seed.seedModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun backupModule() = module {
    includes(seedModule())
    includes(legacyModule())

    singleOf(::ImportUseCase)
}