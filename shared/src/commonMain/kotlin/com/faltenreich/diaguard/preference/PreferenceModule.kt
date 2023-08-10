package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.list.usecase.SetStartScreenUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    singleOf(::GetStartScreenUseCase)
    singleOf(::SetStartScreenUseCase)
    singleOf(::GetStartScreenPreferenceUseCase)
    singleOf(::GetAboutPreferenceUseCase)
    singleOf(::GetAppVersionPreferenceUseCase)

    factory { (preferences: List<Preference>?) -> PreferenceListViewModel(preferences) }
}