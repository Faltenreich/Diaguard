package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.preference.store.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.StartScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class NavigationViewModel(
    dispatcher: CoroutineDispatcher = inject(),
    getStartScreen: GetStartScreenUseCase = inject(),
) : ViewModel<NavigationViewState, Unit>() {

    override val state = getStartScreen().map { startScreen ->
        NavigationViewState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> DashboardScreen
                StartScreen.TIMELINE -> TimelineScreen()
                StartScreen.LOG -> LogScreen()
            },
        )
    }.flowOn(dispatcher)

    override fun onIntent(intent: Unit) = Unit
}