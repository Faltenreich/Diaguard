package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.store.GetAppVersionUseCase
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    singleOf(::GetPreferenceUseCase)
    singleOf(::SetPreferenceUseCase)
    singleOf(::GetAppVersionUseCase)

    singleOf(::GetDefaultPreferencesUseCase)

    factory { (preferences: List<PreferenceListItem>?) ->
        PreferenceListViewModel(preferences)
    }
}