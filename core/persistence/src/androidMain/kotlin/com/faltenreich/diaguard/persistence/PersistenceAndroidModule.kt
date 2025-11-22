package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.persistence.keyvalue.DataStore
import com.faltenreich.diaguard.persistence.keyvalue.FakeKeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.KeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.SharedPreferences
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDiskDriverFactory
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDriverFactory
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightInMemoryDriverFactory
import com.faltenreich.diaguard.persistence.sqlite.SqliteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.dsl.module

const val KEY_VALUE_STORE_LEGACY = "KEY_VALUE_STORE_LEGACY"

internal actual fun persistencePlatformModule(inMemory: Boolean) = module {
    single {
        if (inMemory) FakeKeyValueStore()
        else DataStore(androidContext())
    }
    factoryOf(::SharedPreferences) {
        named(KEY_VALUE_STORE_LEGACY)
        bind<KeyValueStore>()
    }

    single<SqlDelightDriverFactory> {
        if (inMemory) SqlDelightInMemoryDriverFactory()
        else SqlDelightDiskDriverFactory(androidContext())
    }

    factory { SqliteDatabase(androidContext().getDatabasePath("diaguard.db")) }
}