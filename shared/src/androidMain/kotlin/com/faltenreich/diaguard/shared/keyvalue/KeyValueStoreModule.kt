package com.faltenreich.diaguard.shared.keyvalue

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val KEY_VALUE_STORE_LEGACY = "KEY_VALUE_STORE_LEGACY"

actual fun keyValueStoreModule() = module {
    single<KeyValueStore> { DataStore(androidContext()) }
    single<KeyValueStore>(named(KEY_VALUE_STORE_LEGACY)) { SharedPreferences(androidContext()) }
}