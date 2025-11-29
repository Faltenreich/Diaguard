package com.faltenreich.diaguard.startup.legacy

import org.koin.dsl.module

actual fun legacyDaoModule() = module {
    single<LegacyDao> { EmptyLegacyDao() }
}