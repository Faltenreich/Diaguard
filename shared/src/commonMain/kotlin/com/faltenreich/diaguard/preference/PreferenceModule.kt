package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.about.GetAppVersionUseCase
import com.faltenreich.diaguard.preference.list.item.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.item.color.SetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.item.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.list.item.screen.SetStartScreenUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    singleOf(::GetColorSchemeUseCase)
    singleOf(::SetColorSchemeUseCase)
    singleOf(::GetStartScreenUseCase)
    singleOf(::SetStartScreenUseCase)
    singleOf(::GetAppVersionUseCase)

    singleOf(::GetDefaultPreferencesUseCase)

    factory { (preferences: List<Preference>?) ->
        PreferenceListViewModel(preferences)
    }
}