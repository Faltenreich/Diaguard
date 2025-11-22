package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.persistence.file.SystemFileReader
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun persistenceModule(inMemory: Boolean) = module {
    includes(persistencePlatformModule(inMemory))
    factoryOf(::SystemFileReader)
    factoryOf(::SqlDelightDatabase)
}

internal expect fun persistencePlatformModule(inMemory: Boolean): Module