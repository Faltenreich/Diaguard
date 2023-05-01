package com.faltenreich.diaguard.shared.database.sqldelight

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun sqlDelightModule() = module {
    singleOf(::SqlDelightDriverFactory)
}