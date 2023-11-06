package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferences: List<Preference>?,
    getDefaultPreferences: GetDefaultPreferencesUseCase = inject(),
    dispatcher: CoroutineDispatcher = inject(),
) : ViewModel() {

    private val state = (preferences?.let(::flowOf) ?: getDefaultPreferences())
        .map(PreferenceListViewState::Loaded)
        .flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState.Loading,
    )
}