package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetColorSchemePreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetDataPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetMeasurementPreferenceUseCase
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
    getAboutPreference: GetAboutPreferenceUseCase = inject(),
    getTermsAndConditionsPreference: GetTermsAndConditionsPreferenceUseCase = inject(),
    getAppVersionPreference: GetAppVersionPreferenceUseCase = inject(),
) : ViewModel() {

    private val default: Flow<List<Preference>> = combine(
        getColorSchemePreference(),
        getStartScreenPreference(),
        getDataPreference(),
        getMeasurementPreference(),
        getAboutPreference(),
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