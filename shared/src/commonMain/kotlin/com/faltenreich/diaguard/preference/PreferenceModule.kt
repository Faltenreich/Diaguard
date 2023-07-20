package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    // FIXME: Fix constructor
    single { KeyValueStore() }
    singleOf(::PreferenceStore)
    singleOf(::PreferenceListViewModel)
}