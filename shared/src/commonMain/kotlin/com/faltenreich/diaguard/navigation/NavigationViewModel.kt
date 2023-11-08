package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.preference.list.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.list.screen.StartScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NavigationViewModel(
    dispatcher: CoroutineDispatcher = inject(),
    getStartScreen: GetStartScreenUseCase = inject(),
) : ViewModel() {

    private val state = getStartScreen().map { startScreen ->
        NavigationViewState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> Screen.Dashboard
                StartScreen.TIMELINE -> Screen.Timeline()
                StartScreen.LOG -> Screen.Log()
            },
        )
    }.flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = NavigationViewState(
            startScreen = null,
        ),
    )
}