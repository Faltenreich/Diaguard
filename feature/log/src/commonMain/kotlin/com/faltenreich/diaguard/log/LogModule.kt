package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.navigation.navigationModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun logModule() = module {
    includes(
        dataModule(),
        entryModule(),
        navigationModule(),
    )

    factoryOf(::InvalidateLogDayStickyInfoUseCase)

    viewModelOf(::LogViewModel)
}