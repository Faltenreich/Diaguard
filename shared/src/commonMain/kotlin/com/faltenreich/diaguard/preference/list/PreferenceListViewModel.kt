package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.list.item.SelectableOption
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferenceStore: PreferenceStore = inject(),
) : ViewModel() {

    private val startScreen = preferenceStore.startScreen

    private val listItems: Flow<List<Preference>> = startScreen.map { startScreen ->
        listOf(
            Preference.Plain(
                title = { stringResource(MR.strings.version) },
                subtitle = { "4.0.0" },
            ),
            Preference.Selection(
                title = { stringResource(MR.strings.start_screen) },
                subtitle = { stringResource(startScreen.labelResource) },
                options = StartScreen.values().map { value ->
                    SelectableOption(
                        label = { stringResource(value.labelResource) },
                        isSelected = value == startScreen,
                        onSelected = { preferenceStore.setStartScreen(value) },
                    )
                },
            ),
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