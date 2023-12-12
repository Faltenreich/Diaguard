package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.preference.store.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.StartScreen
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
) : ViewModel {

    private val state = getStartScreen().map { startScreen ->
        NavigationViewState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> DashboardScreen
                StartScreen.TIMELINE -> TimelineScreen()
                StartScreen.LOG -> LogScreen()
            },
        )
    }.flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = NavigationViewState(
            startScreen = null,
        ),
    )
}