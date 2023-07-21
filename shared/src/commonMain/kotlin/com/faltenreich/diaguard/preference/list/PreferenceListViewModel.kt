package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferenceStore: PreferenceStore = inject(),
) : ViewModel() {

    private val startScreen = preferenceStore.startScreen
    private val listItems: Flow<List<Preference>> = flowOf(
        listOf(
            Preference.Plain(
                title = "Version",
                subtitle = "4.0.0",
            ),
            Preference.Selection(
                title = "Start Screen",
                subtitle = "",
                options = StartScreen.values().map { startScreen ->
                    startScreen.name to startScreen
                },
                onSelection = preferenceStore::setStartScreen,
            ),
        )
    )
    private val state = listItems.map(::PreferenceListViewState)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState(
            listItems = emptyList(),
        ),
    )
}