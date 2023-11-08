package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.preference.store.about.GetAppVersionUseCase
import com.faltenreich.diaguard.preference.store.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.preference.store.color.SetColorSchemeUseCase
import com.faltenreich.diaguard.preference.store.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.SetStartScreenUseCase
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

    factory { (preferences: List<PreferenceListItem>?) ->
        PreferenceListViewModel(preferences)
    }
}