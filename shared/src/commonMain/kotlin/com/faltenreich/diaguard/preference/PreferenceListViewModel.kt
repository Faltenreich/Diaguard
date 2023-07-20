package com.faltenreich.diaguard.preference

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
    val listItems: Flow<List<PreferenceListItem>> = flowOf(
        listOf(
            PreferenceListItem.Plain(
                title = "Version",
                subtitle = "4.0.0",
            )
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