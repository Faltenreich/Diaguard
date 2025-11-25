package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.decimal.IllustrateDecimalPlacesUseCase
import com.faltenreich.diaguard.preference.food.FoodPreferenceListViewModel
import com.faltenreich.diaguard.preference.license.LicenseListViewModel
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListViewModel
import com.faltenreich.diaguard.data.preference.PreferenceRepository
import com.faltenreich.diaguard.preference.version.GetAppVersionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceRepository)

    factoryOf(::GetPreferenceUseCase)
    factoryOf(::SetPreferenceUseCase)
    factoryOf(::GetAppVersionUseCase)
    factoryOf(::IllustrateDecimalPlacesUseCase)

    viewModelOf(::OverviewPreferenceListViewModel)
    viewModelOf(::FoodPreferenceListViewModel)
    viewModelOf(::LicenseListViewModel)
}