package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.about.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetLicensesPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetPrivacyPolicyPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetSourceCodePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.about.GetTermsAndConditionsPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.color.GetColorSchemePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetContactPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetFacebookPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetHomepagePreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.contact.GetMailPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.data.GetDataPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.data.GetMeasurementPreferenceUseCase
import com.faltenreich.diaguard.preference.list.item.screen.GetStartScreenPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDefaultPreferencesUseCase(
    private val getColorSchemePreference: GetColorSchemePreferenceUseCase,
    private val getStartScreenPreference: GetStartScreenPreferenceUseCase,

    private val getDataPreference: GetDataPreferenceUseCase,
    private val getMeasurementPreference: GetMeasurementPreferenceUseCase,

    private val getContactPreference: GetContactPreferenceUseCase,
    private val getHomepagePreference: GetHomepagePreferenceUseCase,
    private val getMailPreference: GetMailPreferenceUseCase,
    private val getFacebookPreference: GetFacebookPreferenceUseCase,

    private val getAboutPreference: GetAboutPreferenceUseCase,
    private val getSourceCodePreference: GetSourceCodePreferenceUseCase,
    private val getLicensesPreference: GetLicensesPreferenceUseCase,
    private val getPrivacyPolicyPreference: GetPrivacyPolicyPreferenceUseCase,
    private val getTermsAndConditionsPreference: GetTermsAndConditionsPreferenceUseCase,
    private val getAppVersionPreference: GetAppVersionPreferenceUseCase,
) {

    operator fun invoke(): Flow<List<Preference>> {
        return combine(
            getColorSchemePreference(),
            getStartScreenPreference(),
            getDataPreference(),
            getMeasurementPreference(),
            getContactPreference(),
            getHomepagePreference(),
            getMailPreference(),
            getFacebookPreference(),
            getAboutPreference(),
            getSourceCodePreference(),
            getLicensesPreference(),
            getPrivacyPolicyPreference(),
            getTermsAndConditionsPreference(),
            getAppVersionPreference(),
        ) { flows -> flows.map { it } }
    }
}