package com.faltenreich.diaguard.shared.keyvalue

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

const val KEY_VALUE_STORE_LEGACY = "KEY_VALUE_STORE_LEGACY"

actual fun keyValueStoreModule() = module {
    singleOf(::DataStore) bind KeyValueStore::class
    factoryOf(::SharedPreferences) {
        named(KEY_VALUE_STORE_LEGACY)
        bind<KeyValueStore>()
    }
}