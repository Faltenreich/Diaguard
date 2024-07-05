package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormViewModel
import com.faltenreich.diaguard.preference.decimal.IllustrateDecimalPlacesUseCase
import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import org.koin.core.module.dsl.factoryOf
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

    singleOf(::IllustrateDecimalPlacesUseCase)
    factoryOf(::DecimalPlacesFormViewModel)
}