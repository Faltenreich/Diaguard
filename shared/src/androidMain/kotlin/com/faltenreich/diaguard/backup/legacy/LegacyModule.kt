package com.faltenreich.diaguard.backup.legacy

import org.koin.dsl.module

actual fun legacyDaoModule() = module {
    single<LegacyDao> { LegacySqliteDao(database = get(), dateTimeFactory = get()) }
}