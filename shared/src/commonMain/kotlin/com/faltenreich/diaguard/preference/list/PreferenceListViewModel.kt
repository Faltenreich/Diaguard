package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferences: List<Preference>?,
    getStartScreenPreference: GetStartScreenPreferenceUseCase = inject(),
    getAboutPreference: GetAboutPreferenceUseCase = inject(),
    getAppVersionPreference: GetAppVersionPreferenceUseCase = inject(),
) : ViewModel() {

    private val default: Flow<List<Preference>> = combine(
        getStartScreenPreference(),
        getAboutPreference(),
        getAppVersionPreference(),
    ) { getStartScreenPreference, getAboutPreference, appVersionPreference ->
        listOf(
            getStartScreenPreference,
            getAboutPreference,
            appVersionPreference,
            // TODO: Extract into use case
            Preference.Folder(MR.strings.dashboard, listOf(appVersionPreference))
        )
    }

    private val state = (preferences?.let(::flowOf) ?: default).map(::PreferenceListViewState)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState(
            listItems = emptyList(),
        ),
    )
}