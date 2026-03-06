package com.faltenreich.diaguard.persistence

import com.faltenreich.diaguard.config.BuildConfig
import com.faltenreich.diaguard.persistence.database.SqlDelightDiskDriverFactory
import com.faltenreich.diaguard.persistence.database.SqlDelightDriverFactory
import com.faltenreich.diaguard.persistence.database.SqlDelightInMemoryDriverFactory
import com.faltenreich.diaguard.persistence.database.SqliteDatabase
import com.faltenreich.diaguard.persistence.file.AndroidFileOpener
import com.faltenreich.diaguard.persistence.file.FileOpener
import com.faltenreich.diaguard.persistence.keyvalue.DataStore
import com.faltenreich.diaguard.persistence.keyvalue.FakeKeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.KeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val KEY_VALUE_STORE_LEGACY = "KEY_VALUE_STORE_LEGACY"

internal actual fun persistencePlatformModule(inMemory: Boolean) = module {
    single {
        if (get<BuildConfig>().hasPlatformFramework()) DataStore(androidContext())
        else FakeKeyValueStore()
    }
    factoryOf(::SharedPreferences) {
        named(KEY_VALUE_STORE_LEGACY)
        bind<KeyValueStore>()
    }

    single<SqlDelightDriverFactory> {
        if (get<BuildConfig>().hasPlatformFramework()) SqlDelightDiskDriverFactory(androidContext())
        else SqlDelightInMemoryDriverFactory()
    }

    factory { SqliteDatabase(androidContext().getDatabasePath("diaguard.db")) }

    factoryOf(::AndroidFileOpener) bind FileOpener::class
}