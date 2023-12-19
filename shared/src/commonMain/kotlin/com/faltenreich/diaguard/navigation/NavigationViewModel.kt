package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.preference.store.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.StartScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

class NavigationViewModel(
    getStartScreen: GetStartScreenUseCase = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
    private val isNavigatedTo: IsNavigatedToUseCase = inject(),
) : ViewModel<NavigationViewState, NavigationIntent>() {

    override val state = getStartScreen().map { startScreen ->
        NavigationViewState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> DashboardScreen
                StartScreen.TIMELINE -> TimelineScreen()
                StartScreen.LOG -> LogScreen()
            },
        )
    }

    override fun onIntent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.NavigateTo -> navigateTo(intent.screen)
        }
    }

    fun isNavigatedTo(clazz: KClass<out Screen>): Boolean {
        return isNavigatedTo.invoke(clazz)
    }
}