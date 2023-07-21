package com.faltenreich.diaguard.preference

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)
    singleOf(::PreferenceListViewModel)
}