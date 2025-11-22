package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.persistence.file.SystemFileReader
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun persistenceModule(inMemory: Boolean) = module {
    includes(persistencePlatformModule(inMemory))
    factoryOf(::SystemFileReader)
}

internal expect fun persistencePlatformModule(inMemory: Boolean): Module