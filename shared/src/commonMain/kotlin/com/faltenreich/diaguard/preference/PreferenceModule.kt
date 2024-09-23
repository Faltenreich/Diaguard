package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormViewModel
import com.faltenreich.diaguard.preference.decimal.IllustrateDecimalPlacesUseCase
import com.faltenreich.diaguard.preference.license.GetLicensesUseCase
import com.faltenreich.diaguard.preference.license.LicenseListViewModel
import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    singleOf(::GetPreferenceUseCase)
    singleOf(::SetPreferenceUseCase)
    singleOf(::GetAppVersionUseCase)

    singleOf(::GetDefaultPreferencesUseCase)

    singleOf(::IllustrateDecimalPlacesUseCase)
    viewModelOf(::DecimalPlacesFormViewModel)

    // TODO: Use generated file at shared/build/generated/aboutLibraries/<variant>/res/raw
    single { GetLicensesUseCase(fileReader = ResourceFileReader("files/aboutlibraries.json")) }
    viewModelOf(::LicenseListViewModel)

    viewModelOf(::PreferenceListViewModel)
}