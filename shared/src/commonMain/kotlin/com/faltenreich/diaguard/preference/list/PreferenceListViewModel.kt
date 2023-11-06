package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetColorSchemePreferenceUseCase
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
import com.faltenreich.diaguard.preference.list.usecase.GetTermsAndConditionsPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferences: List<Preference>?,
    dispatcher: CoroutineDispatcher = inject(),
    getColorSchemePreference: GetColorSchemePreferenceUseCase = inject(),
    getStartScreenPreference: GetStartScreenPreferenceUseCase = inject(),
    getDataPreference: GetDataPreferenceUseCase = inject(),
    getMeasurementPreference: GetMeasurementPreferenceUseCase = inject(),
    getContactPreference: GetContactPreferenceUseCase = inject(),
    getHomepagePreference: GetHomepagePreferenceUseCase = inject(),
    getMailPreference: GetMailPreferenceUseCase = inject(),
    getFacebookPreference: GetFacebookPreferenceUseCase = inject(),
    getAboutPreference: GetAboutPreferenceUseCase = inject(),
    getSourceCodePreference: GetSourceCodePreferenceUseCase = inject(),
    getLicensesPreference: GetLicensesPreferenceUseCase = inject(),
    getPrivacyPolicyPreference: GetPrivacyPolicyPreferenceUseCase = inject(),
    getTermsAndConditionsPreference: GetTermsAndConditionsPreferenceUseCase = inject(),
    getAppVersionPreference: GetAppVersionPreferenceUseCase = inject(),
) : ViewModel() {

    private val default: Flow<List<Preference>> = combine(
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

    private val state = (preferences?.let(::flowOf) ?: default)
        .map(PreferenceListViewState::Loaded)
        .flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState.Loading,
    )
}