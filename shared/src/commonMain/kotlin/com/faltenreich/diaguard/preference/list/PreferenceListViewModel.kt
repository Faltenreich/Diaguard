package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.usecase.GetAboutPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetAppVersionPreferenceUseCase
import com.faltenreich.diaguard.preference.list.usecase.GetStartScreenPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    getStartScreenPreference: GetStartScreenPreferenceUseCase = inject(),
    getAboutPreference: GetAboutPreferenceUseCase = inject(),
    getAppVersionPreference: GetAppVersionPreferenceUseCase = inject(),
) : ViewModel() {

    private val listItems: Flow<List<Preference>> = combine(
        getStartScreenPreference(),
        getAboutPreference(),
        getAppVersionPreference(),
    ) { getStartScreenPreference, getAboutPreference, appVersionPreference ->
        listOf(
            getStartScreenPreference,
            getAboutPreference,
            appVersionPreference,
        )
    }

    private val state = listItems.map(::PreferenceListViewState)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState(
            listItems = emptyList(),
        ),
    )
}