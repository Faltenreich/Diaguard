package com.faltenreich.diaguard.shared.database.sqldelight

import org.koin.dsl.module

actual fun sqlDelightModule() = module {
    single { SqlDelightDriverFactory() }
}