package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenPreferenceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)
    singleOf(::PreferenceListViewModel)

    factoryOf(::GetStartScreenPreferenceUseCase)
    factoryOf(::GetAppVersionPreferenceUseCase)
}