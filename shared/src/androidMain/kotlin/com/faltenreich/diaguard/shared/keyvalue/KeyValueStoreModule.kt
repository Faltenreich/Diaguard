package com.faltenreich.diaguard.shared.keyvalue

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun keyValueStoreModule() = module {
    single<KeyValueStore> { DataStore(androidContext()) }
}