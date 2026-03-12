package com.faltenreich.diaguard

import com.faltenreich.diaguard.persistence.persistenceModule
import org.koin.dsl.module

internal fun testModule() = module {
    includes(persistenceModule(inMemory = true))
}