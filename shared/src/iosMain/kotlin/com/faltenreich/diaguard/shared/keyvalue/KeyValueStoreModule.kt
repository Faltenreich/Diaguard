package com.faltenreich.diaguard.shared.keyvalue

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun keyValueStoreModule() = module {
    singleOf(::KeyValueStore)
}