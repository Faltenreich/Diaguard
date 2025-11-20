package com.faltenreich.diaguard.persistence.sqldelight

import org.koin.dsl.module

actual fun sqlDelightModule(inMemory: Boolean) = module {
    single<SqlDelightDriverFactory> { TODO("Not yet implemented") }
}