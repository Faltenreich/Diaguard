package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.persistence.file.DeleteFileUseCase
import com.faltenreich.diaguard.persistence.file.FileReader
import com.faltenreich.diaguard.persistence.file.FileRepository
import com.faltenreich.diaguard.persistence.file.OpenFileUseCase
import com.faltenreich.diaguard.persistence.file.ShareFileUseCase
import com.faltenreich.diaguard.persistence.file.SystemFileReader
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun persistenceModule(inMemory: Boolean) = module {
    includes(persistencePlatformModule(inMemory))
    factoryOf(::SystemFileReader) bind FileReader::class
    factoryOf(::OpenFileUseCase)
    factoryOf(::DeleteFileUseCase)
    factoryOf(::ShareFileUseCase)
    factoryOf(::FileRepository)
}

internal expect fun persistencePlatformModule(inMemory: Boolean): Module