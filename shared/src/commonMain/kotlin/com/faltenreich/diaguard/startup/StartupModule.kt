package com.faltenreich.diaguard.startup

import com.faltenreich.diaguard.startup.legacy.legacyModule
import com.faltenreich.diaguard.startup.seed.seedModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun startupModule() = module {
    includes(
        seedModule(),
        legacyModule(),
    )

    factoryOf(::MigrateDataUseCase)

    viewModelOf(::StartupViewModel)
}