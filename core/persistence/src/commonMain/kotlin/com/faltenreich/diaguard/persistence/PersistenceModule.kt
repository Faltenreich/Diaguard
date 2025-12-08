package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.persistence.file.FileReader
import com.faltenreich.diaguard.persistence.file.SystemFileReader
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun persistenceModule(inMemory: Boolean) = module {
    includes(persistencePlatformModule(inMemory))
    factoryOf(::SystemFileReader) bind FileReader::class
}

internal expect fun persistencePlatformModule(inMemory: Boolean): Module