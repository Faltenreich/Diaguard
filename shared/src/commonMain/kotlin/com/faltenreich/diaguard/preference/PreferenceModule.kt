package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.GetDefaultPreferencesUseCase
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.about.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetAppVersionUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetLicensesPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetPrivacyPolicyPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetSourceCodePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetTermsAndConditionsPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.color.GetColorSchemePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.item.color.SetColorSchemeUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetContactPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetFacebookPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetHomepagePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetMailPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.data.GetDataPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.data.GetMeasurementPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.screen.GetStartScreenPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.list.item.screen.SetStartScreenUseCase
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
    singleOf(::GetAppVersionUseCase)
    singleOf(::GetAppVersionPreferenceUseCase)

    singleOf(::GetDefaultPreferencesUseCase)

    factory { (preferences: List<Preference>?) ->
        PreferenceListViewModel(preferences)
    }
}