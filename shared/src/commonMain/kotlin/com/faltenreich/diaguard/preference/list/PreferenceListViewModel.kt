package com.faltenreich.diaguard.preference.list

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
    getStartScreenPreference: GetStartScreenPreferenceUseCase,
    getAppVersionPreference: GetAppVersionPreferenceUseCase = inject(),
) : ViewModel() {

    private val listItems: Flow<List<Preference>> = combine(
        getStartScreenPreference(),
        getAppVersionPreference(),
    ) { getStartScreenPreference, appVersionPreference ->
        listOf(
            appVersionPreference,
            getStartScreenPreference,
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