package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.decimal.IllustrateDecimalPlacesUseCase
import com.faltenreich.diaguard.preference.food.FoodPreferenceListViewModel
import com.faltenreich.diaguard.preference.license.LicenseListViewModel
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListViewModel
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    factoryOf(::GetPreferenceUseCase)
    factoryOf(::SetPreferenceUseCase)
    factoryOf(::GetAppVersionUseCase)
    factoryOf(::IllustrateDecimalPlacesUseCase)

    viewModelOf(::OverviewPreferenceListViewModel)
    viewModelOf(::FoodPreferenceListViewModel)
    viewModelOf(::LicenseListViewModel)
}