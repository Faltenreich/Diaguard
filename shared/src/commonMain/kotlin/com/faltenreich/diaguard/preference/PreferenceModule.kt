package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetColorSchemePreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetContactPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetDataPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetFacebookPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetHomepagePreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetLicensesPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetMailPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetMeasurementPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetPrivacyPolicyPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetSourceCodePreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetTermsAndConditionsPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.SetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.usecase.SetStartScreenUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun preferenceModule() = module {
    singleOf(::PreferenceStore)

    singleOf(::GetColorSchemeUseCase)
    singleOf(::SetColorSchemeUseCase)
    singleOf(::GetColorSchemePreferenceUseCase)
    singleOf(::GetStartScreenUseCase)
    singleOf(::SetStartScreenUseCase)
    singleOf(::GetStartScreenPreferenceUseCase)
    singleOf(::GetDataPreferenceUseCase)
    singleOf(::GetMeasurementPreferenceUseCase)
    singleOf(::GetContactPreferenceUseCase)
    singleOf(::GetHomepagePreferenceUseCase)
    singleOf(::GetMailPreferenceUseCase)
    singleOf(::GetFacebookPreferenceUseCase)
    singleOf(::GetAboutPreferenceUseCase)
    singleOf(::GetSourceCodePreferenceUseCase)
    singleOf(::GetLicensesPreferenceUseCase)
    singleOf(::GetPrivacyPolicyPreferenceUseCase)
    singleOf(::GetTermsAndConditionsPreferenceUseCase)
    singleOf(::GetAppVersionPreferenceUseCase)

    factory { (preferences: List<Preference>?) ->
        PreferenceListViewModel(preferences)
    }
}